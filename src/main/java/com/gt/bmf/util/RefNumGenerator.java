package com.gt.bmf.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RefNumGenerator {
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private DecimalFormat df = new DecimalFormat("0000");
	private int index = 0;

	private static RefNumGenerator refNumGenerator = null;
	

	private RefNumGenerator() {
	}

	
	public synchronized static RefNumGenerator getInstance() {
		if (refNumGenerator == null) {
			refNumGenerator = new RefNumGenerator();
		}
		return refNumGenerator;
	}

	public synchronized String gen() {
		String r = null;
		Date now = new Date();
		r = dateFormat.format(now)+df.format(index);
		index++;
		if(index>9999){
			index=0;
		}
		return r;
	}

}
