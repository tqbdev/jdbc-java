package com.tqbdev.model;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class HocSinh {
	private int MaHS;
	private String TenHS;
	private Date NgaySinh;
	private String Ghichu;
	private byte[] ExtInfo_image;

	public int getMaHS() {
		return MaHS;
	}

	public void setMaHS(int maHS) {
		MaHS = maHS;
	}

	public String getTenHS() {
		return TenHS;
	}

	public void setTenHS(String tenHS) {
		TenHS = tenHS;
	}

	public Date getNgaySinh() {
		return NgaySinh;
	}

	public void setNgaySinh(Date ngaySinh) {
		NgaySinh = ngaySinh;
	}

	public String getGhichu() {
		return Ghichu;
	}

	public void setGhichu(String ghichu) {
		Ghichu = ghichu;
	}

	public BufferedImage getExtInfo_image() throws IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(ExtInfo_image);
		return ImageIO.read(in);
	}
	
	public byte[] getExtInfo_bytes() {
		return ExtInfo_image;
	}

	public void setExtInfo_image(File imageFile) throws IOException {
		ImageInputStream input = ImageIO.createImageInputStream(imageFile);
		Iterator<ImageReader> readers = ImageIO.getImageReaders(input);

		if (readers.hasNext()) {
			ImageReader reader = readers.next();
			reader.setInput(input);

			BufferedImage image = reader.read(0);
			String format = reader.getFormatName();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, format, baos);
			ExtInfo_image = baos.toByteArray();
		}
	}
	
	public void setExtInfo_bytes(byte[] byteArray) {
		ExtInfo_image = byteArray;
	}

	public HocSinh(int maHS, String tenHS, Date ngaySinh, String ghichu, File imageFile) throws IOException {
		super();
		MaHS = maHS;
		TenHS = tenHS;
		NgaySinh = ngaySinh;
		Ghichu = ghichu;

		ImageInputStream input = ImageIO.createImageInputStream(imageFile);
		Iterator<ImageReader> readers = ImageIO.getImageReaders(input);

		if (readers.hasNext()) {
			ImageReader reader = readers.next();
			reader.setInput(input);

			BufferedImage image = reader.read(0);
			String format = reader.getFormatName();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, format, baos);
			ExtInfo_image = baos.toByteArray();
		}
	}
	
	public HocSinh(int maHS, String tenHS, Date ngaySinh, String ghichu, byte[] byteArray) {
		super();
		MaHS = maHS;
		TenHS = tenHS;
		NgaySinh = ngaySinh;
		Ghichu = ghichu;
		ExtInfo_image = byteArray;		
	}
	
	public HocSinh(int maHS, String tenHS, Date ngaySinh, String ghichu) {
		super();
		MaHS = maHS;
		TenHS = tenHS;
		NgaySinh = ngaySinh;
		Ghichu = ghichu;
	}
}
