package edu.berkeley.sixtus.simul;

import edu.berkeley.sixtus.simul.Cell.SlotType;

public class Node {
	
	public static final int MAX_TIME_SLOT = 101;
	public static final int MAX_CH_OFFSET = 16;
	
	private int id;
	private int numAllocated;
	private int numCollisions;
	private int numIdenticallyAllocated;
	private Cell[][] slotframe   = new Cell[MAX_TIME_SLOT][MAX_CH_OFFSET];
	
	private int numNeighbors;
	
	public Node(int id){
		this.id=id;
		for (int i=0;i<MAX_TIME_SLOT;i++){
			for (int j=0;j<MAX_CH_OFFSET;j++){
				slotframe[i][j]= new Cell(i, j, -1, Cell.SlotType.OFF);
			}	
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumAllocated() {
		return numAllocated;
	}

	public void setNumAllocated(int numAllocated) {
		this.numAllocated = numAllocated;
	}

	public int getNumCollisions() {
		return numCollisions;
	}

	public void setNumCollisions(int numCollisions) {
		this.numCollisions = numCollisions;
	}

	public void scheduleLink(int slotNumber, int channelOffset,SlotType type, int node) {
        
 		Cell candidate= new Cell(slotNumber, channelOffset, node, type);
 		//check for collisions
 		Cell ce=slotframe[slotNumber][channelOffset];
 		if (ce.getType()==Cell.SlotType.OFF && ce.getTarget()==-1){
 	    	slotframe[slotNumber][channelOffset]=candidate;
 			this.numAllocated++;
 		}		
	}

	public boolean isLinkAvailable(int slotNumber, int channelOffset,SlotType type, int node) {
		Cell candidate= new Cell(slotNumber, channelOffset, node, type);
 		//check for collisions
 		Cell ce=slotframe[slotNumber][channelOffset];
 		if (ce.getType()==Cell.SlotType.OFF && ce.getTarget()==-1){
 	    	 return true;
 		}else{
 			if (ce.compareTo(candidate)==0){
 				//rescheduling the same cell
 				this.numIdenticallyAllocated++;
 				
 				//System.out.println("Collision occurred by allocating the same cell at slotNumber " + slotNumber + " ch.Offset "+ channelOffset + " at Node " + this.id + " with neighbour " + node + " " +ce);
 				return true;
 			}else{
 				this.numCollisions++;
 			    //System.out.println("Collision occurred at slotNumber " + slotNumber + " ch.Offset "+ channelOffset + " at Node " + this.id + " with neighbour " + node + " " +ce);
 				return false;
 			} 
 		}
 	
	}
	
	public int getNumIdenticallyAllocated() {
		return numIdenticallyAllocated;
	}

	public void setNumIdenticallyAllocated(int numIdenticallyAllocated) {
		this.numIdenticallyAllocated = numIdenticallyAllocated;
	}

	public int getNumNeighbors() {
		return numNeighbors;
	}

	public void setNumNeighbors(int numNeighbors) {
		this.numNeighbors = numNeighbors;
	}

	
	
}
