package edu.berkeley.sixtus.simul;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * 
 * @author xvilajosana
 * 
 */
public class SimulatorEngine {

	public static final int MAX_NET_SIZE = 50;
	public static final int MAX_NUM_NEIGHBORS = 10;
	public static final int MIN_NUM_NEIGHBORS = 2;

	public static final int MAX_TIME_SLOT = 101;
	public static final int MAX_CH_OFFSET = 16;

	public boolean[][] network = new boolean[MAX_NET_SIZE][MAX_NET_SIZE];// matrix matching neighbours
	public Node[] nodes = new Node[MAX_NET_SIZE]; //the nodes
	
	private int num_links_requested;

	public SimulatorEngine() {
      super();
	}

	/**
	 * requires the number of links to be allocated to each neigbour
	 * 
	 * @param links
	 */
	public void simulate(int links) {
		Random ran = new Random();
		this.num_links_requested = links;

		initializeNetwork();

		// create the network randomly.
		createNetworkTopology(ran);
		// compute how many neighbors has each node and update each neighbor with that info
		computeNumNeighbors();

		// for each node in the network request several links to all neighbours
		for (int numlink = 1; numlink < this.num_links_requested; numlink++) {
			System.out.println("************************ requesting " + numlink + " links");
			allocateLinks(ran, numlink);
			printResult(numlink);
			// reset the nodes: for the next iteration
			resetNodes();
			// compute how many neighbors has each node and update each neighbor with that info
			computeNumNeighbors();
		}
	}

	private void allocateLinks(Random ran, int l) {
		for (int k = 0; k < l; k++) {
			for (int i = 0; i < MAX_NET_SIZE; i++) {
				for (int j = 0; j < MAX_NET_SIZE; j++) {
					if (network[i][j]) {
						// these are neighbors
						int slotNumber = ran.nextInt(MAX_TIME_SLOT);// pick random ts and ch.offset
						int channelOffset = ran.nextInt(MAX_CH_OFFSET);
						//TODO, check result of the operation and if false then do not reschedule or reset schedule
						if (nodes[i].isLinkAvailable(slotNumber,channelOffset,Cell.SlotType.TX, j) &&
								nodes[j].isLinkAvailable(slotNumber,channelOffset,Cell.SlotType.TX, j)){
							nodes[i].scheduleLink(slotNumber, channelOffset,Cell.SlotType.TX, j);
							nodes[j].scheduleLink(slotNumber, channelOffset,Cell.SlotType.RX, i);
						}
					}
				}
			}
		}
	}

	private void createNetworkTopology(Random ran) {
		for (int i = 0; i < MAX_NET_SIZE; i++) {
			// select number of neighbors for that node
			int num_nei = ran.nextInt(MAX_NUM_NEIGHBORS);
			if (num_nei < MIN_NUM_NEIGHBORS) {
				num_nei = MIN_NUM_NEIGHBORS;
			}
			// set them in the network structure. pick them randomly too.
			for (int j = 0; j < num_nei; j++) {
				int h = ran.nextInt(MAX_NET_SIZE);
				while (h == i) {
					h = ran.nextInt(MAX_NET_SIZE);// we don't want to say that
													// we are neighbours of
													// ourselves
				}
				network[i][h] = true;// set it as the neighbour
				network[h][i] = true;// set it as the neighbour
			}
		}
	}

	private void initializeNetwork() {
		for (int i = 0; i < MAX_NET_SIZE; i++) {
			for (int j = 0; j < MAX_NET_SIZE; j++) {
				network[i][j] = false; // initialize network
			}
		}
		resetNodes();
	}

	private void resetNodes() {
		for (int i = 0; i < MAX_NET_SIZE; i++) {
			nodes[i] = new Node(i);
		}
	}

	private void printResult(int numlink) {
		String head=null;
		
		if (numlink==1){
			head = "Node,NumLinksRequested,NumNeighbors,TotalLinks,Allocated Links,Collisions,IdenticaAllocation,% Collision,% Used Links";
			System.out.println(head);
		}
			
		for (int i = 0; i < MAX_NET_SIZE; i++) {
			String content = i
					+ ","
					+ numlink
					+ ","
					+ nodes[i].getNumNeighbors()
					+ ","
					+ (MAX_TIME_SLOT * MAX_CH_OFFSET)
					+ ","
					+ nodes[i].getNumAllocated()
					+ ","
					+ nodes[i].getNumCollisions()
					+ ","
					+ nodes[i].getNumIdenticallyAllocated()
					+ ","
					+ ((((double) nodes[i].getNumCollisions()) / ((double) nodes[i].getNumAllocated())) * 100.0)
					+ ","
					+ (((double) nodes[i].getNumAllocated() / (MAX_TIME_SLOT * MAX_CH_OFFSET)) * 100.0);
			
			System.out.println(content);
			
			this.writeToFile("results" + i,head, content);
		}
	}

	public void computeNumNeighbors() {
		for (int i = 0; i < MAX_NET_SIZE; i++) {
			int count = 0;
			for (int j = 0; j < MAX_NET_SIZE; j++) {
				if (network[i][j])
					count++;
			}
			nodes[i].setNumNeighbors(count);
		}
	}

	public void writeToFile(String filename,String header, String content) {
		FileOutputStream fop = null;
		File file;

		try {
			file = new File(filename);

			if (!file.exists()) {
				file.createNewFile();
			}

			fop = new FileOutputStream(file, true);

			if (header!=null) content = header + '\n' + content;
			
			// get the content in bytes
			byte[] contentInBytes = content.getBytes();

			fop.write(contentInBytes);
			fop.write('\n');
			fop.flush();
			fop.close();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
