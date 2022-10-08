package org.eddie.tools.geocaching.common.encoder;

import org.eddie.tools.geocaching.common.Utils;

public class KamasutraEncoder extends ReplacementEncoder {
	
	public KamasutraEncoder(String code, boolean doUpperLower) {
		super(code+Utils.reverse(code),doUpperLower);
	}
	
}
