/*
 * Please see submission instructions for what to write here.
 */

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SentAnalysis {

	protected static HashMap<String, Integer> word_count_pos = new HashMap<String, Integer>();
	protected static HashMap<String, Integer> word_count_neg = new HashMap<String, Integer>();

	protected static int pos_count = 0;
	protected static int neg_count = 0;

	final static File TRAINFOLDER = new File("train");

	public static void main(String[] args) throws IOException
	{
		ArrayList<String> files = readFiles(TRAINFOLDER);

		train(files);
		//if command line argument is "evaluate", runs evaluation mode
		if (args.length==1 && args[0].equals("evaluate")){
			evaluate();
		}
		else{//otherwise, runs interactive mode
			@SuppressWarnings("resource")
			Scanner scan = new Scanner(System.in);
			System.out.print("Text to classify>> ");
			String textToClassify = scan.nextLine();
			System.out.println("Result: "+classify(textToClassify));
		}

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

		//System.out.println("Positive Hashmap: " + word_count_pos);
		//System.out.println("Negative Hashmap: " + word_count_neg);

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

	public static double log2(double N)
    {
        // calculate log2 N indirectly
        // using log() method
        double result = (Math.log(N) / Math.log(2));

        return result;
    }

	/*
	 * Classifier: Classifies the input text (type: String) as positive or negative
	 */
	public static String classify(String text){
		//get all individual words & get them in right format
		text = text.replaceAll("\\p{Punct}", "");
		text = text.toLowerCase();

		ArrayList<String> words = new ArrayList<String>();
		Scanner s = new Scanner(text);
		s.useDelimiter(" +");
		while (s.hasNext()){
			words.add(s.next());
		}

		//initialize our results to 0
		double prob_result_pos = 0;
		double prob_result_neg = 0;

		//calculate the Pr(positive) = #positive reviews / total # reviews
		double pr_positive = (double)word_count_pos.size()/(double)(word_count_pos.size() + word_count_neg.size());
		//calculate the Pr(negative) = #negative reviews / total # reviews
		double pr_negative = (double)word_count_neg.size()/(double)(word_count_pos.size() + word_count_neg.size());


		prob_result_pos = log2(pr_positive);
		//System.out.println("Initial: " + prob_result_pos);

		prob_result_neg = log2(pr_negative);

		double n_word_pos = 0;
		double n_word_neg = 0;

		for (int w = 0; w < words.size(); w++){
			//System.out.println("Word: " + words.get(w));
			double curr_conditionalprob_neg = 0;
			double curr_conditionalprob_pos = 0;
			if (word_count_pos.get(words.get(w)) == null){
				//System.out.println(w + " is NULL in positive");
				n_word_pos = 0;
			}
			if(word_count_neg.get(words.get(w)) == null){
				//System.out.println(w + " is NULL in negative");
				n_word_neg = 0;
			}
			if (word_count_neg.get(words.get(w)) != null){
				n_word_neg = word_count_neg.get(words.get(w));
			}
			if (word_count_pos.get(words.get(w)) != null) {
				n_word_pos = word_count_pos.get(words.get(w));
			}

			double lambda = 0.0001;

			curr_conditionalprob_neg = (n_word_neg + lambda) / (word_count_neg.size() + (words.size() * lambda));
			//System.out.println(curr_conditionalprob_neg);
			curr_conditionalprob_pos = (n_word_pos + lambda) / (word_count_pos.size() + (words.size() * lambda));
			//System.out.println(curr_conditionalprob_pos);


			prob_result_neg += log2(curr_conditionalprob_neg);
			//System.out.println("Cummulative Neg: " + prob_result_neg);
			prob_result_pos += log2(curr_conditionalprob_pos);
			//System.out.println("Pos: " + prob_result_pos);


		}


		String result="";

		if (prob_result_neg >= prob_result_pos){
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
		for (int i = 0; i < filesToClassify.size(); i++){

			String inputtext = "";

			Path file = Paths.get(foldername+"/"+filesToClassify.get(i));
			try {
				inputtext = Files.readString(file);
			}
			catch (IOException e) {
            	e.printStackTrace();
        	}
			String result = classify(inputtext);	//classify this input text as positive or negative
			System.out.println("File: " + foldername + "/" + filesToClassify.get(i));	//print what file we're classifying
			System.out.println("Result: " + result);	//print the classification
		}

	}



}
