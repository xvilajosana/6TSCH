'''
Created on 27/06/2013

@author: xvilajosana
'''

import random
import Cell
import Node
import Constants as c

class SimulatorEngine(object):
    '''
    classdocs
    '''

    def __init__(self):
        '''
        Constructor
        '''
        self.network = [[False for x in xrange(c.MAX_NET_SIZE)] for x in xrange(c.MAX_NET_SIZE)] 
    
        
        
    def simulate(self,links):
        
        self.initializeNetwork()
        self.createNetworkTopology()
        self.computeNumNeighbors()
        
        for numLink in range(links):
            print "** requesting Link {0}".format(numLink)
            self.allocateLinks(numLink)
            self.printResult(numLink)
            self.resetNodes()
            self.computeNumNeighbors()
            
    def allocateLinks(self,numLink):
        for k in range(numLink):
            for i in range(c.MAX_NET_SIZE):
                for j in range(c.MAX_NET_SIZE):
                    if self.network[i][j]==True:
                        slotNumber=random.randint(0,c.MAX_TIME_SLOT-1)
                        chOffset=random.randint(0,c.MAX_CH_OFFSET-1)
                        #print "slotNum={0}, chOff={1}".format(slotNumber,chOffset)
                        
                        if ((self.nodes[i].isLinkAvailable(slotNumber,chOffset,c.CELL_TYPE_TX,j) and self.nodes[j].isLinkAvailable(slotNumber,chOffset,c.CELL_TYPE_RX,i))):
                            self.nodes[i].scheduleLink(slotNumber, chOffset,c.CELL_TYPE_TX, j)
                            self.nodes[j].scheduleLink(slotNumber, chOffset,c.CELL_TYPE_RX, i)
                               
                            
                                                        
    def createNetworkTopology(self):
        for i in range(c.MAX_NET_SIZE):
            # select number of neighbors for that node
            num_nei = random.randint(c.MIN_NUM_NEIGHBORS,c.MAX_NUM_NEIGHBORS-1)
            # set them in the network structure. pick them randomly too.
            for j in range(num_nei):
                h = random.randint(0,c.MAX_NET_SIZE-1)
                while (h == i):
                    h = random.randint(0,c.MAX_NET_SIZE-1)# we don't want to say that we are neighbours of ourselves
                self.network[i][h] = True;# set it as the neighbour
                self.network[h][i] = True;# set it as the neighbour

   
   
   
    def initializeNetwork(self):
        for i in range(c.MAX_NET_SIZE):
            for j in range(c.MAX_NET_SIZE):
                self.network[i][j] = False # initialize network
        self.resetNodes();

    def resetNodes(self):
        self.nodes = [Node.Node(count) for count in range(c.MAX_NET_SIZE)]
        


    def printResult(self,numlink):
        head=None

        if numlink==1:
            head = "Node,NumLinksRequested,NumNeighbors,TotalLinks,Allocated Links,Collisions,IdenticaAllocation,% Collision,% Used Links"
            print head

        for i in range(c.MAX_NET_SIZE):
            content = "{0},{1},{2},{3},{4},{5}".format(numlink,self.nodes[i].getNumNeighbors(),c.MAX_TIME_SLOT * c.MAX_CH_OFFSET,self.nodes[i].getNumAllocated(),self.nodes[i].getNumCollisions(),self.nodes[i].getNumIdenticallyAllocated())
            
            print content
            
            
            
    def computeNumNeighbors(self):
        for i in range(c.MAX_NET_SIZE):
            count = 0
            for j in range(c.MAX_NET_SIZE):
                if self.network[i][j]==True:
                    count=count+1
            self.nodes[i].setNumNeighbors(count);
