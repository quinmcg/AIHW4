/*
 * Please see submission instructions for what to write here.
 */

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class SentAnalysis {

	protected static HashMap<String, Integer> word_count_pos = new HashMap<String, Integer>();
	protected static HashMap<String, Integer> word_count_neg = new HashMap<String, Integer>();

	protected static int pos_count = 0;
	protected static int neg_count = 0;

	final static File TRAINFOLDER = new File("testingtrain");

	public static void main(String[] args) throws IOException
	{
		ArrayList<String> files = readFiles(TRAINFOLDER);

		train(files);
		//if command line argument is "evaluate", runs evaluation mode
		// if (args.length==1 && args[0].equals("evaluate")){
		// 	evaluate();
		// }
		// else{//otherwise, runs interactive mode
		// 	@SuppressWarnings("resource")
		// 	Scanner scan = new Scanner(System.in);
		// 	System.out.print("Text to classify>> ");
		// 	String textToClassify = scan.nextLine();
		// 	System.out.println("Result: "+classify(textToClassify));
		// }

	}



	/*
	 * Takes as parameter the name of a folder and returns a list of filenames (Strings)
	 * in the folder.
	 */
	public static ArrayList<String> readFiles(File folder){

		System.out.println("Populating list of files");

		//List to store filenames in folder
		ArrayList<String> filelist = new ArrayList<String>();


		for (File fileEntry : folder.listFiles()) {
	        String filename = fileEntry.getName();
	        filelist.add(filename);
		}

		/*
		for (String fileEntry : filelist) {
	        System.out.println(fileEntry);
		}

		System.out.println(filelist.size());
		*/


		return filelist;
	}




	/*
	 * TO DO
	 * Trainer: Reads text from data files in folder datafolder and stores counts
	 * to be used to compute probabilities for the Bayesian formula.
	 * You may modify the method header (return type, parameters) as you see fit.
	 */
	public static void train(ArrayList<String> files) throws FileNotFoundException
	{
		Pattern pos_file = Pattern.compile("[a-z]*-5-[0-9]*");

		for(int file = 0; file < files.size(); file++){
			if(Pattern.matches("([a-z])*-5-([0-9])*.txt", files.get(file))){
				populateMap(files.get(file), word_count_pos);
				pos_count++;
			}
			else{
				populateMap(files.get(file), word_count_neg);
				neg_count++;
			}
		}

		System.out.println("Positive Hashmap: " + word_count_pos);
		System.out.println("Negative Hashmap: " + word_count_neg);

	}

	public static void populateMap(String file_name, HashMap<String, Integer> hashmap) throws FileNotFoundException{
		File review = new File("train/"+file_name);
		Scanner input = new Scanner(review);

		input.useDelimiter(" +");

		while(input.hasNext()){
			int value = 0;
			String next_word = input.next();

			next_word = next_word.replaceAll("\\p{Punct}", "");
			next_word = next_word.toLowerCase();

			if(hashmap.get(next_word) == null){
				value = 1;
			}else{
				value = hashmap.get(next_word);
				value++;
			}

			hashmap.put(next_word, value);
		}

	}


	/*
	 * Classifier: Classifies the input text (type: String) as positive or negative
	 */
	public static String classify(String text)
	{
		//initialize our results to 0
		double prob_result_pos = 0;
		double prob_result_neg = 0;

		//PROBABILITY TEXT IS POSITIVE SENTIMENT:

		//calculate the Pr(positive) = #positive reviews / total # reviews
		double pr_positive = ;



		//PROBABILITY TEXT IS NEGATIVE SENTIMENT:

		//calculate the Pr(negative) = #negative reviews / total # reviews
		double pr_negative = ;



		String result="";

		if (prob_result_neg > prob_result_pos){
			result = "negative";
		}
		else{
			result = "positive";
		}

		return result;

	}




	/*
	 * TO DO
	 * Classifier: Classifies all of the files in the input folder (type: File) as positive or negative
	 * You may modify the method header (return type, parameters) as you like.
	 */
	public static void evaluate() throws FileNotFoundException
	{
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);

		System.out.print("Enter folder name of files to classify: ");
		String foldername = scan.nextLine();
		File folder = new File(foldername);

		ArrayList<String> filesToClassify = readFiles(folder);

		// Iterate over all files in filesToClassify
		// call classify on each individual file
		// store that result in an ArrayList of "positive" and "negative" classifications
		// return the ArrayList

	}



}
