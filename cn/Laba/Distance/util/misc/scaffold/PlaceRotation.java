package cn.Laba.Distance.util.misc.scaffold;

import cn.Laba.Distance.util.math.Rotation;
import cn.Laba.Distance.util.misc.scaffold.blocks.PlaceInfo;

public final class PlaceRotation {

	private final PlaceInfo placeInfo;

	private final Rotation rotation;

	public PlaceInfo getPlaceInfo() {
		return this.placeInfo;
	}

	public Rotation getRotation() {
		return this.rotation;
	}

	public PlaceRotation(PlaceInfo placeInfo, Rotation rotation) {
		this.placeInfo = placeInfo;
		this.rotation = rotation;
	}
	
}
