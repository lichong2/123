package com.dingfeng.qiniu;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.commons.fileupload.FileItem;
import net.coobird.thumbnailator.Thumbnails;

public class ImageUtils {
	//定义要压缩文件的大小值
	private static final int MAX_SIZE = 500 * 1024;
	//定义要压缩后的大小比例
	private static final float AFT_QUALITY = 0.4f;
	//定义要压缩后图片长宽高的比例
	private static final double AFT_SCALE = 0.4;
	//上传的文件格式
	private static final String[] fileFormat = {"jpeg","jpg","gif","png"};
	
	/**
	 * 进行图片质量压缩和图片的长宽压缩，但不改变长宽比
	 * @return	BufferedImage
	 */
	public static BufferedImage getBufferImage(FileItem fileItem){
		BufferedImage bufferedImage = null;
		try {
			if(fileItem.getSize() > MAX_SIZE){
				bufferedImage = Thumbnails.of(fileItem.getInputStream()).outputQuality(AFT_QUALITY).scale(AFT_SCALE).asBufferedImage();
			}else{
				bufferedImage = Thumbnails.of(fileItem.getInputStream()).scale(1).asBufferedImage();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bufferedImage;
	}
	
	/**
	 * 把BufferedImage写成byte[]的格式
	 * @param bufferedImage
	 * @param type		图片格式
	 * @return	byte[]
	 */
	public static byte[] bufferImageToArrayByte(BufferedImage bufferedImage, String type){
		byte[] data = null;
		ByteArrayOutputStream os = new ByteArrayOutputStream();  
        try {
			ImageIO.write(bufferedImage, type, os);
			data = os.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	/**
	 * 判断该文件类型是否为上述定义的文件类型
	 * @param fileItem
	 * @return	boolean  true是    false否
	 */
	public static boolean checkFileIsImage(FileItem fileItem){
		boolean flag = false;
		String type = fileItem.getName().substring(fileItem.getName().lastIndexOf(".")+1);
		System.out.println(type);
		for(int i = 0; i < fileFormat.length; i++){
			String type1 = fileFormat[i];
			if(type.equals(type1)){
				flag = true;
				break;
			}
		}
		return flag;
	}
}
