package com.steve.app;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

/**
 * Entry point for the application.
 *
 */
public class App {
	public static int totalImagesWithFaces = 0;
	public static int currentProcessedFile = 0;
	public static int totalFiles = 0;
	static String casscadefileLocation = "/home/damoklesh/workspace/opencv-3.4.2/data/haarcascades/haarcascade_frontalface_default.xml";
	static String imagesDir = "/home/damoklesh/workspace/testimg";
	static String imageDestination = "/home/damoklesh/workspace/JImgFace/JImgFace/destination/";

	public static void main(String[] args) {
		paramInitialization(args);

		System.out.println("Initiating Detection...");

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		// TEST FILE ITERATION
		FileWalker fWalker = new FileWalker();
		List<Path> images = new ArrayList<Path>();

		try {
			Stream<Path> imageFiles = fWalker.extractImageFilesFromDir(imagesDir);
			images = imageFiles.collect(Collectors.toList());
			totalFiles = images.size();
			System.out.println(totalFiles + " Images Found");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Load cascasde classifier
		CascadeClassifier faceClassifier = new CascadeClassifier(casscadefileLocation);

		images.forEach(img -> {
			currentProcessedFile++;
			Mat src = Imgcodecs.imread(img.toString());
			DetectFaceForImage(src, img.getFileName().toString(), faceClassifier);
		});

		System.out.println("Image Detection Finished");
		System.out.println("Total images with faces: " + totalImagesWithFaces);

	}

	private static void paramInitialization(String[] args) {
		if (args.length == 3) {
			System.out.println("Using custom parameters");
			casscadefileLocation = args[0];
			imagesDir = args[1];
			imageDestination = args[2];
		} else {
			System.out.println("Using Default parameters");
		}
	}

	private static void DetectFaceForImage(Mat src, String fileName, CascadeClassifier faceClassifier) {

		try {
			// Convert to grey scale
			Mat grayFrame = new Mat();
			Imgproc.cvtColor(src, grayFrame, Imgproc.COLOR_BGR2GRAY);
			Imgproc.equalizeHist(grayFrame, grayFrame);

			// Face detection
			MatOfRect faceDetection = new MatOfRect();
			faceClassifier.detectMultiScale(src, faceDetection);
			System.out.println(String.format("Processed file number: %d", currentProcessedFile));
//			System.out.println(String.format("Progress %s percent", (totalFiles/currentProcessedFile)/100));

			if (!faceDetection.empty()) {
				Imgcodecs.imwrite(imageDestination + fileName, src);
				totalImagesWithFaces++;
				System.out.println(String.format("Detected face in file: %s", fileName));
				System.out.println("File Added.");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
