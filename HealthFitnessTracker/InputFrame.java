import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.File;
import java.lang.NumberFormatException;

public class InputFrame extends JFrame implements ActionListener{
	//CONSTANTS
	public static final String DEFAULT_USERFILE = "userName.txt";
	public static final String MENU_LINES ="======================="
	+"============================================================";
	public static final int DEFAULT_NUM_BUTTONS = 2;
	public static final int DEFAULT_WIDTH = 1400;
	public static final int DEFAULT_HEIGHT = 1000;
	
	//INSTANCE VARIABLES/FIELDS
	private UserInfo userInput;
	private JPanel statsInput;
	private String gender;
	private String activityChoice;
	private ArrayList<String> userNames;
	private ArrayList<String> associatedFile;
	private ArrayList<String> genderList;
	private ArrayList<Double> activityMET; 
	private JTextArea exerciseJournal;
	private JTextField[] entry;
	private String loginInfo;
	private ButtonGroup choices;
	private ButtonGroup activityGroup;
	private JLabel pictureHolder;
	private String weightType;
	private String heightType;
	private JTextField minutesExercising;
	private JComboBox exerciseList;
	
	//DESC: initial formatting of GUI. uses JOptionPane to handle
	//user login/new users
	public InputFrame(){
		super();
		//today's date
		String todaysDate = getTodaysDate().toString();
		
		userNames = new ArrayList<String>(5);
		associatedFile = new ArrayList<String>(5);
		genderList = new ArrayList<String>(5);


		this.setTitle("HTP: Info Entry Form");
		this.setSize(500, 400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Object question= "Welcome to the Health Tracker program.\nPlease choose one of the following:";
		String[] loginButtons = {"Returning User", "New User"};
		ImageIcon fitnessIcon = new ImageIcon("FitnessIcon.png");
		//dialog box with Returning User or New User option
		int userChoice = JOptionPane.showOptionDialog(null, question, "HFT: Login As", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, fitnessIcon, loginButtons, loginButtons[0]);
		
		//create the array for the OptionPane to show		
		//loads the list of known program users and their gender
		readUserFile(DEFAULT_USERFILE);
		String[] names = new String[userNames.size()];
		for (int n = 0; n < names.length; n++){
			names[n] = userNames.get(n);
		}
		//Returning User 
		if (userChoice == JOptionPane.YES_OPTION){
			Object login = "Login as...";
			this.loginInfo = (String)JOptionPane.showInputDialog(null, login, "Health Fitness Tracker", JOptionPane.QUESTION_MESSAGE, fitnessIcon, names, names[0]);
			String[] temp = loginInfo.split(" ");
			String searchName = ""+temp[0] +" "+ temp[1];
			//find index of the particular name chosen
			int index = userNames.indexOf(searchName);
			//sets the gender as parallel to the genderList
			if (index != -1){
				gender = genderList.get(index);
				if (gender.equals("Male")){
					userInput = new Male();
				}
				else{
					userInput = new Female();
				}
			}
			userInput.setFirstName(temp[0]);
			userInput.setLastName(temp[1]);
			//runs the Jframe modification
			fitnessPanel();
			
		}
		else if (userChoice == JOptionPane.NO_OPTION){
				//runs the frame to input new user info
				init();
				
		}
		//exits if you click the x button
		else{
			System.exit(0);
		}
		
		
		
	}
	//DESC: defined the appearance of the JFrame and methods run
	//for new user input
	public void init(){
		statsInput = new JPanel(new GridLayout(0,2));
		//keeps the format of fill-ins and buttons intact
		this.setResizable(false);
		
		JLabel topHeader = new JLabel("Information Entry...");
		statsInput.add(topHeader);
		//keeps the grid  layout proper...
		statsInput.add(new JLabel());
		String[] labelEntry = {"First Name", "Last Name ", "Height    ", "Weight   ", "Age       "};
		entry = new JTextField[5];
		int n = 0;
		//create two new label and text field based on String array
		while (n != labelEntry.length && n < 2){
			JPanel twoItemsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			entry[n] = new JTextField(10);
			twoItemsPanel.add(new JLabel(labelEntry[n]));
			twoItemsPanel.add(entry[n]);
			statsInput.add(twoItemsPanel);
			n++;
		}
		//FlowLayout.LEFT left justify
		JPanel threeItemsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		entry[2] = new JTextField(5);
		threeItemsPanel.add(new JLabel(labelEntry[2]));
		threeItemsPanel.add(entry[2]);
		String[] heightChoices = {"Choose one...", "Inches", "Meters"};
		JComboBox weightTypeList = new JComboBox(heightChoices);
		weightTypeList.setSelectedIndex(0);
		weightTypeList.addActionListener(this);
		threeItemsPanel.add(weightTypeList);
		statsInput.add(threeItemsPanel);
		
		String[] weightChoices = {"Choose one...", "Pounds", "Kilogram"};
		entry[3] = new JTextField(5);
		threeItemsPanel.add(new JLabel(labelEntry[3]));
		threeItemsPanel.add(entry[3]);
		
		JComboBox heightTypeList = new JComboBox(weightChoices);
		heightTypeList.setSelectedIndex(0);
		heightTypeList.addActionListener(this);
		threeItemsPanel.add(heightTypeList);
		statsInput.add(threeItemsPanel);
		
		JPanel twoItemsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		entry[4] = new JTextField(10);
		twoItemsPanel.add(new JLabel(labelEntry[4]));
		twoItemsPanel.add(entry[4]);
		statsInput.add(twoItemsPanel);
		
		JPanel maleOrFemalePanel = new JPanel();
		JRadioButton maleRadioButton = new JRadioButton("Male");
		maleRadioButton.setActionCommand("Male");
		JRadioButton femaleRadioButton = new JRadioButton("Female");
		femaleRadioButton.setActionCommand("Female");
		
		//has to be grouped so they are associated with each other
		choices = new ButtonGroup();
		choices.add(maleRadioButton);
		choices.add(femaleRadioButton);
		//but still add it directly to the JPanel afterwards!
		maleOrFemalePanel.add(maleRadioButton);
		maleOrFemalePanel.add(femaleRadioButton);
		statsInput.add(maleOrFemalePanel);
		
		maleRadioButton.addActionListener(this);
		femaleRadioButton.addActionListener(this);
		
		JPanel activityOptionPanels = new JPanel(new GridLayout (4,0));
		ButtonGroup activityGroup = new ButtonGroup();
		String[] activity = {"Sedentary", "Moderate", "Active", "Very active"};
			for (int m = 0; m < activity.length; m++){
			JRadioButton activityButton = new JRadioButton(activity[m]);
			activityButton.setActionCommand(activity[m]);
			activityGroup.add(activityButton);
			activityOptionPanels.add(activityButton);
			activityButton.addActionListener(this);
		}
		statsInput.add(activityOptionPanels);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setMaximumSize(new Dimension(100,100));
		JButton enter = new JButton("ENTER");
		JButton clear = new JButton("CLEAR");
		buttonPanel.add(enter);
		buttonPanel.add(clear);
		enter.addActionListener(this);
		clear.addActionListener(this);
		
		statsInput.add(buttonPanel);
		
		this.add(statsInput);
	

	}
	//the Frame format for after person info input/returning user
	public void fitnessPanel(){
		this.getContentPane().removeAll();
		this.setTitle("Health Tracker Program");
		this.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		this.setLayout(new BorderLayout());
		//cancels out the resizable(false) in init()
		this.setResizable(true);
		JPanel insidePanel = new JPanel();
		//inside panel contains the pictures
		insidePanel.setLayout(new GridLayout(0,1));
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		ImageIcon pictureBMI = new ImageIcon("bodyMass.png");
		pictureHolder = new JLabel(pictureBMI); 
		insidePanel.add(topPanel);
		
		ImageIcon chartBMI = new ImageIcon("BMIchart.png");
		topPanel.add(new JLabel(chartBMI));
		topPanel.add(pictureHolder);
		
		//text panels contains the text area
		JPanel textPanel = new JPanel();
		JTextArea healthJournal = new JTextArea(28, 70);
		ArrayList<String> journalFile = new ArrayList<String>(10);
		healthJournal.setEditable(false);
		//read the particular user file
		String fileName = "user"+userInput.getFirstName()+userInput.getLastName()+".txt";
		journalFile = this.readJournalFile(fileName);
		for(String element: journalFile){
			healthJournal.append(element +"\n");
		}
		
		
		JScrollPane scrollText = new JScrollPane(healthJournal);
		scrollText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollText.setVisible(true);
		textPanel.add(scrollText);
		
		JPanel bottomPanel = new JPanel();
		JButton updateButton = new JButton("Update Info");
		updateButton.addActionListener(this);
		bottomPanel.add(updateButton);
		
		//activity panel is in the center and has the activity calculations
		JPanel exercisePanel = new JPanel(new BorderLayout());
		JPanel topExercisePanel = new JPanel(new GridLayout(0,2));
		ArrayList<String> activityList = new ArrayList<String>(10);
		activityMET = new ArrayList<Double>(10);
		//reads from METvalues.txt and fill in ArrayList
		ArrayList<String> temporary = this.readJournalFile("METvalues.txt");
		for(String element: temporary){
			String[] parts = element.split(";");
			activityList.add(parts[0]);
			activityMET.add(Double.parseDouble(parts[1]));
			
		}
		
		minutesExercising = new JTextField("put minutes (ex. 60) of the activity here!");
		//populate the JComboBox with read from filed exercises
		exerciseList = new JComboBox(activityList.toArray());
		exerciseJournal = new JTextArea(18, 50);
		exerciseJournal.setText(MENU_LINES +"\nMET is defined "+
		" as the energy expended"+ " by an individual sitting.\n"+
		"To calculate calories/minutes"+ ", use the"+" equation: MET "+
		"* minutes worked * 3.5 * kg body weight / 200.\n"+MENU_LINES);
		exerciseJournal.setLineWrap(true);
		exerciseJournal.setEditable(false);
		JScrollPane scrollExercise = new JScrollPane(exerciseJournal);
		scrollExercise.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		JButton calculateExercise = new JButton("Calculate Calories");
		calculateExercise.addActionListener(this);
		topExercisePanel.add(exerciseList);
		topExercisePanel.add(minutesExercising);
		exercisePanel.add(topExercisePanel, BorderLayout.NORTH);
		exercisePanel.add(scrollExercise, BorderLayout.CENTER);
		exercisePanel.add(calculateExercise, BorderLayout.SOUTH);
		this.add(insidePanel, BorderLayout.NORTH);
		this.add(textPanel, BorderLayout.WEST);
		this.add(bottomPanel, BorderLayout.SOUTH);
		this.add(exercisePanel, BorderLayout.CENTER);
		
		
		
	}
	//DESC: used to put up the update panel that allows the user
	//to put new height/weight/age and activity level.
		public void updatePanel(){
		this.getContentPane().removeAll();
		this.setTitle("Update Information");
		this.setSize(600, 400);
		this.setLayout(new BorderLayout());
		
		//keeps the format of the update fill-in proper
		this.setResizable(false);
		//update this later
		entry = new JTextField[3];
		
		JPanel updateInsidePanel = new JPanel(new GridLayout(0,1));
		updateInsidePanel.add(new JLabel("Update Information Below:"));
		String[] updateInfo = {"Height    ", "Weight   ", "Age       "};
		
		String[] heightChoices = {"Choose one...", "Inches", "Meters"};
		JPanel threeItemsPanel = new JPanel();
		entry[0] = new JTextField(5);
		threeItemsPanel.add(new JLabel(updateInfo[0]));
		threeItemsPanel.add(entry[0]);
		JComboBox weightTypeList = new JComboBox(heightChoices);
		weightTypeList.setSelectedIndex(0);
		weightTypeList.addActionListener(this);
		threeItemsPanel.add(weightTypeList);
		updateInsidePanel.add(threeItemsPanel);
		
		
		String[] weightChoices = {"Choose one...", "Pounds", "Kilogram"};
		entry[1] = new JTextField(5);
		threeItemsPanel.add(new JLabel(updateInfo[1]));
		threeItemsPanel.add(entry[1]);
		JComboBox heightTypeList = new JComboBox(weightChoices);
		heightTypeList.setSelectedIndex(0);
		heightTypeList.addActionListener(this);
		threeItemsPanel.add(heightTypeList);
		updateInsidePanel.add(threeItemsPanel, BorderLayout.CENTER);

		
		JPanel twoItemsPanel = new JPanel();
		entry[2] = new JTextField(10);
		twoItemsPanel.add(new JLabel(updateInfo[2]));
		twoItemsPanel.add(entry[2]);
		updateInsidePanel.add(twoItemsPanel);
		
		JPanel activityOptionPanels = new JPanel(new GridLayout (4,0));
		activityOptionPanels.add(new JLabel("Set your activity level"));
		activityOptionPanels.add(new JLabel(""));
		ButtonGroup activityGroup = new ButtonGroup();
		String[] activity = {"Sedentary", "Moderate", "Active", "Very active"};
		for (int m = 0; m < activity.length; m++){
		JRadioButton activityButton = new JRadioButton(activity[m]);
		activityButton.setActionCommand(activity[m]);
		activityGroup.add(activityButton);
		activityOptionPanels.add(activityButton);
		activityButton.addActionListener(this);
		}
		
		
		JButton updateButton = new JButton ("Update");
		updateButton.addActionListener(this);

		this.add(updateInsidePanel, BorderLayout.NORTH);
		this.add(activityOptionPanels, BorderLayout.CENTER);
		this.add(updateButton, BorderLayout.SOUTH);
		
		
		
	}
	
	
	//users click enter or clear button
	@Override
	public void actionPerformed(ActionEvent e){
		String typeData = null;
		Object action = e.getSource();
		//important portion as JComboBox and JRadioButton does not
		//allow mismatch casting. Thus by figuring out its instanceof
		//it can run the proper set of actions.
		if(action instanceof JComboBox){
			JComboBox heightOrWeight = (JComboBox) action;
			typeData = (String) heightOrWeight.getSelectedItem();
		}
		else if (action instanceof JButton){
			JButton buttons = (JButton)action;
			typeData = (String) buttons.getText();
		}
		else{
			typeData = e.getActionCommand();
		}
		//this is for the fitnessPanel(after fill in/returning user)
		if (typeData.equals("Calculate Calories")){
			try{
			int minutes = Integer.parseInt(minutesExercising.getText());
			int indexChosen = exerciseList.getSelectedIndex();
			if (indexChosen > 0){
				writeActivityMET(indexChosen);
			}
			}
			catch(NumberFormatException nfe){
				minutesExercising.setText("Need numbers!");
			}
		}
		if(typeData.equals("Update Info")){
			updatePanel();
		}
		//this is associated with update panel/method
		if (typeData.equals("Update")){
			if (activityChoice == null){
				System.out.println("Please pick activity level!");
			}
			else if (weightType != null && heightType != null){
				
				String updateLog = this.updateMethod();
				this.writeJournalEntry(updateLog);
				this.fitnessPanel();
			}
			else{
				System.out.println("Please pick weight/height type");
			}
		}
		//this is for the init(), associated with user profile entry
		if (typeData.equals("Male")){
			gender = "Male";
			
		}
		else if(typeData.equals("Female")){
			gender = "Female";
		}
		if(typeData.equals("Kilogram")){
			weightType = "Kilogram";
		}
		else if (typeData.equals("Pounds")){
			weightType = "Pounds";
		}
		if (typeData.equals("Meters")){
			heightType = "Meters";
		}
		else if(typeData.equals("Inches")){
			heightType = "Inches";
		}
		if(typeData.equals("Sedentary")){
			activityChoice = "Sedentary";
		}
		else if (typeData.equals("Moderate")){
			activityChoice = "Moderate";;
		}
		else if (typeData.equals("Active")){
			activityChoice = "Active";
		}
		else if (typeData.equals("Very active")){
			activityChoice = "Very active";
		}
		if(typeData.equals("CLEAR")){
			for (JTextField element: entry)
			{
				element.setText("");
			}
			choices.clearSelection();
			
		}
		else if(typeData.equals("ENTER")){
			if(allFilled(entry)){
				//need to cast UserInfo since it's abstract
				//can't create an object of it
				if (gender.equals("Male")){
					userInput = new Male();
				}
				else if(gender.equals("Female")){
					userInput = new Female();
				}
			}
			if (fillIn(entry)){
				//write in the information in userName.txt
				writeUserFile(userInput);
				//create the initial journal entry
				writeInitialJournalFile(userInput);
				statsInput.removeAll();
				//change the size
				fitnessPanel();
			}
			else{
				System.out.println("Incomplete Form...");
			}
			
			
		}
	}
	//DESC: determine today's date
	//return String in the format stated within SimpleDateFormat
	public String getTodaysDate(){
		DateFormat todaysDate = new SimpleDateFormat("MMMMM dd, yyyy");
		Date day = new Date();
		String today = todaysDate.format(day);
		return today;
	}
	//DESC: updateMethod takes in the user input within update frame opt
	//PRE: the heightType, weightType, and activity level has been set
	//POST: returns String representing all the updates(including calc.)
	// and necessary nutrients info. Also writes it out in the journal.
	public String updateMethod()
	{	
		String today = getTodaysDate();
		double convertedHeight = 0;
		double convertedWeight = 0;
		int age = 0;
		try{
			convertedHeight = Integer.parseInt(entry[0].getText());
				age = Integer.parseInt(entry[2].getText());
				if (heightType.equals("Meters")){
					convertedHeight = convertedHeight * 39.370;
				}
				convertedWeight = Integer.parseInt(entry[1].getText());
				if (weightType.equals("Kilogram")){
					convertedWeight =convertedWeight * 2.2046;
				}
				userInput.setHeight((int)convertedHeight);
				userInput.setWeight(convertedWeight);
				userInput.setAge(age);
		}
		catch(NullPointerException nfe){
			System.out.println("NEED ENTRIES!");
		}
		catch(NumberFormatException e){
			System.out.println("numbers only please!");
		}
	
		String updateLog= String.format("%nThis update entry was made on"+
		":%s%n%nNew Height:%.0f "+"new Weight:%.2f and new Age:%d%n%n", 
		today, convertedHeight, convertedWeight, age);
		String healthUpdate = healthInformationEntry(userInput);
		String nutrientUpdate = nutrientRequirements(userInput);
		
		return updateLog + nutrientUpdate+ healthUpdate + MENU_LINES;
	}
	//DESC: fill in the an object of UserInfo (userInput) with all
	//the information from JTextField within new user data entry.
	//PRE: the values are correct types
	//POST: returns boolean whether or not it was set properly.
	//also catches NullPointerException if there is nothing in fields.
	public boolean fillIn(JTextField[] entry){
		try{
			userInput.setFirstName(entry[0].getText());
			userInput.setLastName(entry[1].getText());
			userInput.setGender(gender);
			
			String height = entry[2].getText();
			double convertedHeight = Integer.parseInt(height);
			if (heightType.equals("Meters")){
				convertedHeight = convertedHeight * 39.370;
			}
			String weight = entry[3].getText();
			double convertedWeight = Integer.parseInt(weight);
			if (weightType.equals("Kilogram")){
				convertedWeight =convertedWeight * 2.2046;
			}
			String age = entry[4].getText();
			return userInput.setHeight((int)convertedHeight)&&userInput.setWeight(convertedWeight)&& userInput.setAge(Integer.parseInt(age));
		}
		catch(NullPointerException nfe){
			System.out.println("Input needed!");
		}
		catch(NumberFormatException e){
			System.out.println("numbers only please!");
		}
		return false;
	}
	//DESC: make sure all the fields are filled.
	//PRE: gender and activity choice is not null;
	//POST: returns boolean if it is all filled.
	private boolean allFilled(JTextField[] entry){
		int countFilled = 0;
		for (int n = 0; n < entry.length; n++){
			if (!(entry[n].getText().equals(""))){
				countFilled++;
			}
		}
		if (countFilled == 5 && gender != null && activityChoice !=null){
			return true;
		}
		else{
			return false;
		}
	}
	//DESC: write journal entry/set text area with the MET information.
	//PRE: the textArea minutesExercising has numbers.
	//POST: writes in journal and set text in the textArea.
	private void writeActivityMET(int indexChosen){
		int minutes = Integer.parseInt(minutesExercising.getText());
		String activityName = exerciseList.getSelectedItem().toString();
		String METoutput = String.format("The MET value of the activity"+
		" %s is %.1f %nThe time you worked for is %d minutes"+
		"%n%s%n", activityName, activityMET.get(indexChosen), minutes, MENU_LINES);
		exerciseJournal.append(METoutput);
		this.writeJournalEntry(METoutput);
		
	}
	//method used to write entry within the userName.txt
	// file with gender and associated file.
	private void writeUserFile(UserInfo user){
		
		String name = user.getFirstName() +" "+ user.getLastName() +";"+
		user.getGender();
		String fileName = ";user"+ user.getFirstName()+user.getLastName()+".txt";
		PrintWriter outputStream = null;
		
		try{
			outputStream = new PrintWriter(new FileOutputStream (DEFAULT_USERFILE, true));
			
		}
		catch (FileNotFoundException fnfe){
			System.exit(0);
		}
		outputStream.print(name);
		outputStream.println(fileName);

		outputStream.close();
	}
	//DESC: allows the writing of String within the particular journal
	private void writeJournalEntry(String entry){
		String fileName = "user"+ userInput.getFirstName()+userInput.getLastName()+".txt";
		PrintWriter outputStream = null;
		try{
				outputStream = new PrintWriter(new FileOutputStream (fileName, true));
			
			}
			catch (FileNotFoundException fnfe){
				System.exit(0);
			}
			outputStream.println(entry);
			outputStream.close();
	}
	//create the initial file when the user is a new user.
	private void writeInitialJournalFile(UserInfo user){
		ArrayList<String> template;
		template =  new ArrayList<String>(10);
		template = readJournalFile("template.txt");
		PrintWriter outputStream = null;
		String fileName = "user"+ user.getFirstName()+user.getLastName()+".txt";
		File userFile = new File(fileName);
		if (!(userFile.exists())){
			try{
				userFile.createNewFile();
			}
			catch(IOException ioe){
				System.out.println("ERROR: File:" + fileName+ " cannot be written");
			}
			try{
				outputStream = new PrintWriter(new FileOutputStream (fileName));
			
			}
			catch (FileNotFoundException fnfe){
				System.exit(0);
			}
			for (String element: template){
				outputStream.println(element);
			}
			outputStream.println("Printable journal is under the file:"+ fileName);
			outputStream.println();
			outputStream.println(entryTemplate(user));
			outputStream.println();
			outputStream.println(healthInformationEntry(user));
			outputStream.println();
			outputStream.println(nutrientRequirements(user));
			outputStream.println();
			outputStream.println(MENU_LINES);
			outputStream.close();
			}
		else{
			System.out.println("File already exists!");
		}
	}
	//DESC: format the String for entry	
	private String entryTemplate(UserInfo user){
		String todaysDate = this.getTodaysDate();
		String firstName = user.getFirstName();
		String lastName = user.getLastName();
		//gender
		double height =  user.getHeight();
		double weight = user.getWeight();
		int age = user.getAge();
		String entry = String.format("Date of Entry: %s%nName: %s %s Height:%.2f Weight:%.2f Age:%d", todaysDate, firstName,lastName,height,weight,age);
		
		return entry;
	}
	//DESC: calculate the estimated nutrient values needed.
	//PRE: the male/female value has been set.
	//POST: returns a String representing the estimated values.
	private String nutrientRequirements(UserInfo user){
		String nutrient = null;
		if (gender.equals("Male")){
			Male male = new Male();
			male= (Male)userInput.clone();
			male.setActivityLevel(activityChoice);
			male.getStats();
			//cleaner formatting
			String carbs = male.recommendedCarbohydrate();
			String fat = male.recommendedFat();
			String protein = male.recommendedProtein();
			nutrient = String.format("%s%n%n%s%n%n%s%n", carbs, fat, protein);
		}
		else if (gender.equals("Female")){
			Female male = new Female();
			male= (Female)userInput.clone();
			male.setActivityLevel(activityChoice);
			male.getStats();
			//cleaner formatting
			String carbs = male.recommendedCarbohydrate();
			String fat = male.recommendedFat();
			String protein = male.recommendedProtein();
			nutrient = String.format("%s%n%n%s%n%n%s%n", carbs, fat, protein);
		}
		return nutrient;
	}
	//DESC: format the rest of the calculation including BMI and EER
	//PRE: male/female variable has been set.
	//POST: returns String of formatted health info.
	private String healthInformationEntry(UserInfo user){

		String health = null;
		if (gender.equals("Male")){
			Male male = new Male();
			male= (Male)userInput.clone();
			male.setActivityLevel(activityChoice);
			male.getStats();
			//cleaner formatting
			double BMI = male.getBodyMassIndex();
			double basalMetabolic = male.getBasalRate();
			double energyExpend = male.getEnergyExpenditureRequirement();
			health = String.format("BMI:%.2f Basal Metabolic Rate: %.2f Estimated Energy Requirement: %.2f.%n", BMI, basalMetabolic, energyExpend);
		}
		else if (gender.equals("Female")){
			Female female = new Female();
			female = (Female) userInput.clone();
			female.setActivityLevel(activityChoice);
			female.getStats();
			double BMI = female.getBodyMassIndex();
			double basalMetabolic = female.getBasalRate();
			double energyExpend = female.getEnergyExpenditureRequirement();
			health = String.format("BMI:%.2f Basal Metabolic Rate: %.2f%n Estimated Energy Requirement: %.2f.%n", BMI, basalMetabolic, energyExpend);
		}
			
		return health;
	}
	//DESC: read the journal file and returns ArrayList representing
	//the lines
	//PRE: fileName actually exists
	//POST: return the ArrayList of the lines.
	private ArrayList<String> readJournalFile(String fileName){
		ArrayList<String> journalLog = new ArrayList<String>(10);
		BufferedReader inputStream;
		inputStream = null;
		String temp= null;
		try{
			inputStream = new BufferedReader(new FileReader(fileName));
			while ((temp = inputStream.readLine()) != null)
			{
				journalLog.add(temp);
			}
			inputStream.close();
		}
		catch(FileNotFoundException fnfe){
			System.out.println("ERROR: File:" + fileName+ " cannot be found");
		}
		catch(IOException ioe){
			System.out.println("ERROR: File:" + fileName+ " cannot be read");
		}
		return journalLog;
		
		
	}
	//DESC: splits the DEFAULT_FILE and fill it into the various ArrayList
	//POST: fill in the proper ArrayList with information from userName.txt
	private void readUserFile(String fileName){
		BufferedReader inputStream;
		String[] parts;
		String temp;
		
		inputStream = null;
		temp= null;
		try{
			inputStream = new BufferedReader(new FileReader(fileName));
			while ((temp = inputStream.readLine()) != null)
			{
				parts = temp.split(";");
				userNames.add(parts[0]);
				genderList.add(parts[1]);
				associatedFile.add(parts[2]);
				
			}
		}
		catch(FileNotFoundException fnfe){
			System.out.println("ERROR: File:" + fileName+ " cannot be found");
		}
		catch(IOException ioe){
			System.out.println("ERROR: File:" + fileName+ " cannot be read");
		}
		
		
	}
}
