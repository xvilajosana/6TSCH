'''
Created on 27/06/2013

@author: xvilajosana
'''




class Cell(object):
    '''
    classdocs
    '''
    def __init__(self,timeslot,choff,Ctype,target):
        '''
        Constructor
        '''
        self.slotNumber=timeslot
        self.channelOffset=choff
        self.type=Ctype
        self.target=target
        
        
    def getSlotNumber(self):
        return self.slotNumber

    def setSlotNumber(self, slotNumber):
        self.slotNumber = slotNumber
    
    def getChannelOffset(self):
        return self.channelOffset

    def setChannelOffset(self,channelOffset):
        self.channelOffset = channelOffset

    def getTarget(self):
        return self.target

    def setTarget(self, target) :
        self.target = target

    def getType(self):
        return self.type

    def setType(self, Ctype) :
        self.type = Ctype

    def compareTo(self, o):
        if (self.slotNumber==o.slotNumber and self.channelOffset==o.channelOffset and self.type==o.type and self.target==o.target):
            return 0
        return 1



