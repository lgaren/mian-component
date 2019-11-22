package com.dsglyy.annotation;

public enum SeckillProduct {
	
	PRODUCT1(-1,"price1"),PRODUCT2(-1,"price2"),PRODUCT3(-1,"price3"),
	PRODUCT4(-1,"price4"),PRODUCT5(-1,"price5"),PRODUCT6(-1,"price6"),
	PRODUCT7(-1,"price7"),PRODUCT8(-1,"price8"),PRODUCT9(-1,"price9"),
	PRODUCT10(-1,"price10"),PRODUCT11(-1,"price11"),PRODUCT12(-1,"price12"),
	PRODUCT13(-1,"price13"),PRODUCT14(-1,"price14"),PRODUCT15(-1,"price15"),
	PRODUCT16(-1,"price16"),PRODUCT17(-1,"price17"),PRODUCT18(-1,"price18"),
	PRODUCT19(-1,"price19"),PRODUCT20(-1,"price20");
	
	private int id;
	private String price;
	
	private SeckillProduct( int id,String price) {
		this.id = id;
		this.price = price;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPriceField() {
		return price;
	}

	public void setPriceField(String price) {
		this.price = price;
	}
	
}
