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

    //HASHMAP FOR POSITIVE AND NEGATIVE WORDS (WORD: #TIMES APPEARED IN RESPECTIVE FILES)
    protected static HashMap<String, Integer> word_count_pos = new HashMap<String, Integer>();
    protected static HashMap<String, Integer> word_count_neg = new HashMap<String, Integer>();

    //TOTAL NUMBER OF POSITIVE AND NEGATIVE REVIEWS
    protected static int pos_count = 0;
    protected static int neg_count = 0;

    final static File TRAINFOLDER = new File("train");

    //TOTALS FOR CALCULATING ACCURACY AND PRECISION
    public static double correct_pos = 0;
    public static double correct_neg = 0;
    public static double total_pos = 0;
    public static double total_neg = 0;


    public static void main(String[] args) throws IOException
    {
        ArrayList<String> files = readFiles(TRAINFOLDER);

        train(files);
        //if command line argument is "evaluate", runs evaluation mode
        if (args.length==1 && args[0].equals("evaluate")){
            evaluate();

            //FINAL ACCURACY / PRECISION PRINT STATEMENTS
            System.out.println("Total Accuracy: " + 100*((correct_neg + correct_pos)/(total_neg+total_pos)) + "%");
            System.out.println("Positive precision: " + 100*(correct_pos/total_pos) + "%");
            System.out.println("Negative precision: " + 100*(correct_neg/total_neg) +"%");
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
            //Check if the file name is in the "Positive" format
            if(Pattern.matches("([a-z])*-5-([0-9])*.txt", files.get(file))){

                //Populate the positive hashmap
                populateMap(files.get(file), word_count_pos);
                //increase count of total positive reviews
                pos_count++;
            }
            else{
                //populate negative hashmap
                populateMap(files.get(file), word_count_neg);
                //increase count of total negative reviews
                neg_count++;
            }
        }

    }

    //FUNCTION TO POPULATE THE TXT FILES INTO OUR POS AND NEG HASHMAPS
    public static void populateMap(String file_name, HashMap<String, Integer> hashmap) throws FileNotFoundException{
        File review = new File("train/"+file_name);
        Scanner input = new Scanner(review);

        //IF 1 OR MORE SPACES SEPARATES THE WORDS
        input.useDelimiter(" +");

        //while there are still words in the text file
        while(input.hasNext()){
            int value = 0;
            //get the next words
            String next_word = input.next();

            //REMOVE PUNCTUATION AND MAKE LOWER CASE
            next_word = next_word.replaceAll("\\p{Punct}", "");
            next_word = next_word.toLowerCase();

            //IF WORD DOESNT EXIST, ADD IT TO THE HASHMAP
            if(hashmap.get(next_word) == null){
                value = 1;
            }else{
                //else, increment the current count by 1
                value = hashmap.get(next_word);
                value++;
            }

            //put the word back in the hashmap/update the value
            hashmap.put(next_word, value);
        }

    }

    //OUR FUNCTION FOR CALCULATING LOG BASE 2 USING NATURAL LOG AND LOG RULES
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
        //get all individual words & get them in right format (no PUNCTUATION and all LOWERCASE)
        text = text.replaceAll("\\p{Punct}", "");
        text = text.toLowerCase();

        //Scanner + get the input as individual strings
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

        //log2 the Pr(Positive)
        prob_result_pos = log2(pr_positive);

        //log2 the Pr(Negative)
        prob_result_neg = log2(pr_negative);

        //initialize the number of negative nad positive words
        double num_word_pos = 0;
        double num_word_neg = 0;

        for (int w = 0; w < words.size(); w++){
            double curr_conditionalprob_neg = 0;
            double curr_conditionalprob_pos = 0;

            //if word is not in the positive list
            if (word_count_pos.get(words.get(w)) == null){
                num_word_pos = 0;
            }
            //if word is not in the negative list
            if(word_count_neg.get(words.get(w)) == null){
                num_word_neg = 0;
            }
            //if its in the negative list
            if (word_count_neg.get(words.get(w)) != null){
                num_word_neg = word_count_neg.get(words.get(w));
            }
            //if its in the positive list
            if (word_count_pos.get(words.get(w)) != null) {
                num_word_pos = word_count_pos.get(words.get(w));
            }

            //SMOOTHING LAMBDA
            double lambda = 0.0001;

            curr_conditionalprob_neg = (num_word_neg + lambda) / (word_count_neg.size() + (words.size() * lambda));
            curr_conditionalprob_pos = (num_word_pos + lambda) / (word_count_pos.size() + (words.size() * lambda));

            //update the probability of the whole statement being Positive/Negative by adding the log2(A|f)
            //log2 for the underflow error
            prob_result_neg += log2(curr_conditionalprob_neg);
            prob_result_pos += log2(curr_conditionalprob_pos);


        }


        String result="";

        //if the negative result is strictly greater than positive probability:
        if (prob_result_neg > prob_result_pos){
            result = "negative";
        }
        else{//if the positive is >= negative probability, then we say positive
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

            //Classify call to decide sentiment of input text as positive or negative
            String result = classify(inputtext);



            //***USED FOR EVALUATION:***
            // if the file is positive
            if(Pattern.matches("([a-z])*-5-([0-9])*.txt", filesToClassify.get(i))){
                //if we CORRECTLY classify as positive:
                if(result.equals("positive")){
                    //increase our correctly classified count for positive
                    correct_pos++;
                }
                //increase our total positives
                total_pos++;
            }
            else{//if the file is negative:
                //if we CORRECTLY classify as negative:
                if(result.equals("negative")){
                    //increase our correctly classified count for negative
                    correct_neg++;
                }
                //increase our total negatives
                total_neg++;
            }
        }

    }



}