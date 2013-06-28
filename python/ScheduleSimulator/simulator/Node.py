'''
Created on 27/06/2013

@author: xvilajosana
'''

import Constants as c
import Cell

class Node(object):
    '''
    classdocs
    '''

    def __init__(self, Nid):
        '''
        Constructor
        '''
        self.id = Nid
        self.numAllocated = 0
        self.numCollisions = 0
        self.numIdenticallyAllocated = 0
        self.numNeighbors = 0
        
        self.slotframe = [[Cell.Cell(x, y, c.CELL_TYPE_OFF,-1) for y in xrange(c.MAX_CH_OFFSET)] for x in xrange(c.MAX_TIME_SLOT)]
        

    def getId(self):
        return self.id;

    def setId(self, Nid):
        self.id = Nid
    
    def getNumAllocated(self):
        return self.numAllocated
    
    
    def setNumAllocated(self, numAllocated):
        self.numAllocated = numAllocated;
    
    
    def getNumCollisions(self):
        return self.numCollisions
    
    def setNumCollisions(self, numCollisions) :
        self.numCollisions = numCollisions;


    def scheduleLink(self, slotNumber, channelOffset, Ctype, node) :
            
        candidate = Cell.Cell(slotNumber, channelOffset, Ctype, node);
        #check for collisions
        ce = self.slotframe[slotNumber][channelOffset];
      
        if (ce.getType() == c.CELL_TYPE_OFF and ce.getTarget() == -1):
            self.slotframe[slotNumber][channelOffset] = candidate;
            self.numAllocated = self.numAllocated + 1

    def isLinkAvailable(self, slotNumber, channelOffset, Ctype, node):
        candidate = Cell.Cell(slotNumber, channelOffset, Ctype, node);
        #check for collisions
        ce = self.slotframe[slotNumber][channelOffset];
        if ce.getType() == c.CELL_TYPE_OFF and ce.getTarget() == -1:
            return True;
        else:
            if (ce.compareTo(candidate) == 0):
                # rescheduling the same cell
                self.numIdenticallyAllocated = self.numIdenticallyAllocated + 1
     
                return True
            else:
                self.numCollisions = self.numCollisions + 1
                return False;

    
    def getNumIdenticallyAllocated(self):
        return self.numIdenticallyAllocated
    
    
    def setNumIdenticallyAllocated(self, numIdenticallyAllocated) :
        self.numIdenticallyAllocated = numIdenticallyAllocated;
    
    
    def getNumNeighbors(self):
        return self.numNeighbors
    
    def setNumNeighbors(self, numNeighbors) :
        self.numNeighbors = numNeighbors;


    
