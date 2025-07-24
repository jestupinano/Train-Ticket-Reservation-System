package com.shashi.beans;

import java.io.Serializable;
import java.sql.Date;

public class BookingDetails implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String mailId;
	private Long tr_no;
	private Date date;
	private String from_stn;
	private String to_stn;
	private int seats;
	private Double amount;

	public String getMailId() {
		return mailId;
	}

	public void setMailId(String mailId) {
		this.mailId = mailId;
	}

	public Long getTr_no() {
		return tr_no;
	}

	public void setTr_no(Long tr_no) {
		this.tr_no = tr_no;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getFrom_stn() {
		return from_stn;
	}

	public void setFrom_stn(String from_stn) {
		this.from_stn = from_stn;
	}

	public String getTo_stn() {
		return to_stn;
	}

	public void setTo_stn(String to_stn) {
		this.to_stn = to_stn;
	}

	public int getSeats() {
		return seats;
	}

	public void setSeats(int seats) {
		this.seats = seats;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

}
