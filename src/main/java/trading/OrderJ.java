package trading;

import java.math.BigDecimal;

public final class OrderJ {
	private final BigDecimal volume;
	private final BigDecimal volumeRestriction;
	
	public OrderJ(BigDecimal volume, BigDecimal volumeRestriction) {
		this.volume = volume;
		this.volumeRestriction = volumeRestriction;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	public BigDecimal getVolumeRestriction() {
		return volumeRestriction;
	}
	
	
}
