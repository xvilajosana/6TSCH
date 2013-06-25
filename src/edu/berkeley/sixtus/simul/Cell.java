package edu.berkeley.sixtus.simul;
/**
 * 
 * @author xvilajosana
 *
 */
public class Cell implements Comparable<Cell> {

	public enum SlotType {
		TX, RX, TXRX, OFF
	}
	
	private int slotNumber;
	private int channelOffset;
	private int target;
	private SlotType type;
	

	public Cell(int slotNumber, int channelOffset, int target, SlotType type) {
		super();
		this.slotNumber = slotNumber;
		this.channelOffset = channelOffset;
		this.target = target;
		this.type = type;
	}
	
	public int getSlotNumber() {
		return slotNumber;
	}
	public void setSlotNumber(int slotNumber) {
		this.slotNumber = slotNumber;
	}
	public int getChannelOffset() {
		return channelOffset;
	}
	public void setChannelOffset(int channelOffset) {
		this.channelOffset = channelOffset;
	}
	public int getTarget() {
		return target;
	}
	public void setTarget(int target) {
		this.target = target;
	}
	public SlotType getType() {
		return type;
	}
	public void setType(SlotType type) {
		this.type = type;
	}

	public String toString(){
		return "CELL:[slot: " + slotNumber + " ch: " + channelOffset + " target: " + target + " type: " +type+"]";
	}

	@Override
	public int compareTo(Cell o) {
		if (this.slotNumber==o.slotNumber &&
				this.channelOffset==o.channelOffset&&
				this.type==o.type&&
				this.target==o.target){
			return 0;
		}
		
		return 1;
		
	}
	
}
