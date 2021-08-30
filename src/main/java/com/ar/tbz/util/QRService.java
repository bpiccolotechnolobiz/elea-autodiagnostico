package com.ar.tbz.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.util.EnumMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.ar.tbz.services.BusquedaService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.pdf.BarcodeQRCode;

@Component
public class QRService {

	private static Log log = LogFactory.getLog(BusquedaService.class);

//	public static void main(String[] args) {
//		new QRService().generateQR();
//	}

	public static String imgToBase64String(final RenderedImage img) {
		final ByteArrayOutputStream os = new ByteArrayOutputStream();

		try {
			ImageIO.write(img, "png", os);
			return java.util.Base64.getEncoder().encodeToString(os.toByteArray());
		} catch (final IOException ioe) {
			throw new UncheckedIOException(ioe);
		}
	}

	public static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}

		// Create a buffered image with transparency
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		// Draw the image on to the buffered image
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		// Return the buffered image
		return bimage;
	}

	public static BufferedImage fromItextImage(com.itextpdf.text.Image img) {
		BarcodeQRCode qrcode = new BarcodeQRCode("testo testo testo", 1, 1, null);
		Image image = qrcode.createAwtImage(Color.BLACK, Color.WHITE);

		return new BufferedImage(image.getWidth(null), image.getWidth(null), BufferedImage.TYPE_4BYTE_ABGR);

	}

	public com.itextpdf.text.Image generateQR() throws BadElementException, MalformedURLException, IOException {
		String myCodeText = "ELEA";
		int size = 512;
		BufferedImage eleaImage = null;
		com.itextpdf.text.Image img = null;
		try {

			Map<EncodeHintType, Object> eleaHintType = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
			eleaHintType.put(EncodeHintType.CHARACTER_SET, "UTF-8");

			// Now with version 3.4.1 you could change margin (white border size)
			eleaHintType.put(EncodeHintType.MARGIN, 1); /* default = 4 */
			Object put = eleaHintType.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

			QRCodeWriter mYQRCodeWriter = new QRCodeWriter(); // throws com.google.zxing.WriterException
			BitMatrix eleaBitMatrix = mYQRCodeWriter.encode(myCodeText, BarcodeFormat.QR_CODE, size, size,
					eleaHintType);
			int eleaWidth = eleaBitMatrix.getWidth();

			// The BufferedImage subclass describes an Image with an accessible buffer of
			// crunchifyImage data.
			eleaImage = new BufferedImage(eleaWidth, eleaWidth, BufferedImage.TYPE_INT_RGB);

			// Creates a Graphics2D, which can be used to draw into this BufferedImage.
			eleaImage.createGraphics();

			// This Graphics2D class extends the Graphics class to provide more
			// sophisticated control over geometry, coordinate transformations, color
			// management, and text layout.
			// This is the fundamental class for rendering 2-dimensional shapes, text and
			// images on the Java(tm) platform.
			Graphics2D graphics = (Graphics2D) eleaImage.getGraphics();

			// setColor() sets this graphics context's current color to the specified color.
			// All subsequent graphics operations using this graphics context use this
			// specified color.
			graphics.setColor(Color.white);

			// fillRect() fills the specified rectangle. The left and right edges of the
			// rectangle are at x and x + width - 1.
			graphics.fillRect(0, 0, eleaWidth, eleaWidth);

			// TODO: Please change this color as per your need
			graphics.setColor(Color.BLUE);

			for (int i = 0; i < eleaWidth; i++) {
				for (int j = 0; j < eleaWidth; j++) {
					if (eleaBitMatrix.get(i, j)) {
						graphics.fillRect(i, j, 1, 1);
					}
				}
			}

			// A class containing static convenience methods for locating
			// ImageReaders and ImageWriters, and performing simple encoding and decoding.
			// ImageIO.write(eleaImage, eleaFileType, eleaFile);

			log.info("\nCongratulation.. You have successfully created QR Code.. \n" + "Check your code here: "
//					+ filePath
			);

			int newW = 100;
			int newH = 100;
			Image tmp = eleaImage.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
			BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

			Graphics2D g2d = dimg.createGraphics();
			g2d.drawImage(tmp, 0, 0, null);
			g2d.dispose();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(dimg, "PNG", baos);

			img = com.itextpdf.text.Image.getInstance(baos.toByteArray());
			return img;
		} catch (WriterException e) {
			log.info("\nSorry.. Something went wrong...\n");
			e.printStackTrace();
		}
		return img;
	}
}
