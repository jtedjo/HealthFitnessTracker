import java.util.ArrayList;
public abstract class UserInfo implements Cloneable{
	public static final String DEFAULT_FIRST_NAME = "John";
	public static final String DEFAULT_LAST_NAME = "Doe";
	public static final int DEFAULT_HEIGHT = 60;
	public static final double DEFAULT_WEIGHT = 140.0;
	public static final int DEFAULT_AGE = 30;
	public static final String DEFAULT_GENDER = "Male";
	
	//Instance Variable
	private String firstName;
	private String lastName;
	private int height;//in inches
	private double weight;//in pounds
	private int age;
	private String gender;
	private double bodyMassIndex;
	
	//DESC: Full constructor using setAll to set.
	public UserInfo(String firstName, String lastName, int height, double weight, int age, String gender){
		setAll(firstName, lastName, height, weight, age, gender);
	}
	//DESC: default constructor, set values to DEFAULT values.
	public UserInfo(){
		setAll(DEFAULT_FIRST_NAME, DEFAULT_LAST_NAME, DEFAULT_HEIGHT, DEFAULT_WEIGHT, DEFAULT_AGE, DEFAULT_GENDER);
	}
	//setters
	//DESC: setters for all information w/in userInfo. 
	//PRE: height & weight & age are valid (greater than 0)
	//POST: set all the parameters to the instance variable.
	public void setAll(String firstName, String lastName, int height, double weight, int age, String gender){
		if (setHeight(height) && setWeight(weight) &&setAge(age))
		{
			calculateBodyMassIndex(weight, height);
			this.firstName = firstName;
			this.lastName = lastName;
			this.setGender(gender);
		}
		else
		{
			System.out.println("One of the components is invalid!");
		}
	}
	//sets first name with String value in parameter.
	public void setFirstName(String firstName){
		this.firstName = firstName;
	}
	//sets last name with String value.
	public void setLastName(String lastName){
		this.lastName = lastName;
	}
	//DESC: sets height if it is valid (>0)
	//returns boolean if it is set or not.
	public boolean setHeight(int height){
		if (height > 0){
			this.height = height;
			return true;
		}
		else{
			System.out.println("Improper height value!");
			return false;
		}
	}
	//DESC: sets weight if it is valid (>0)
	//returns boolean if it is set or not.
	public boolean setWeight(double weight){
		if (weight >0){
			this.weight = weight;
			return true;
		}
		else{
			System.out.println("Improper weight value!");
			return false;
		}
	}
	//DESC: sets age if it is valid (>0)
	//returns boolean if it is set or not.
	public boolean setAge(int age){
		if (age > 0){
			this.age = age;
			return true;
		}	
		else{
			System.out.println("Improper age value");
			return false;
		}
	}
	//DESC: set gender variable with the String inside parameter.
	public void setGender(String gender){
		this.gender = gender;
		
	}
	public boolean setBodyMassIndex(double bodyMassIndex){
		if (bodyMassIndex >0){
			this.bodyMassIndex = bodyMassIndex;
			return true;
		}
		else{
			return false;
		}
	}
	public String getFirstName(){
		return firstName;
	}
	public String getLastName(){
		return lastName;
	}
	public int getHeight(){
		return height;
	}
	public double getWeight(){
		return weight;
	}
	public int getAge(){
		return age;
	}
	public String getGender(){
		return gender;
	}
	public double getBodyMassIndex(){
		return bodyMassIndex;
	}
	//DESC: equals method, check whether an object is equal to an object
	//of UserInfo.
	//POST: returns boolean if it is equal or not.
	@Override
	public boolean equals(Object other){
		if (other == null || !(other instanceof UserInfo)){
			return false;
		}
		else{
			UserInfo otherUser = (UserInfo) other;
			return this.firstName.equals(otherUser.firstName) &&
			this.lastName.equals(otherUser.lastName) && 
			this.height == otherUser.height && this.weight == otherUser.weight &&
			this.age == otherUser.age && this.gender.equals(otherUser.gender) &&
			this.bodyMassIndex == otherUser.bodyMassIndex;
			
		}
	}
	//conversion methods to turn pound to kilo.
	public double convertPoundToKilo(double pound){
		return (pound/2.2046);
	}
	//conversion methods to turn inches to meters.
	public double convertInchesToMeter(int inches){
		double meter = inches * .0254;
		return (meter);
	}
	//DESC: clone method, all immutable objects so simple override.
	//POST: return Object object with the same info as the caller.
	//need to be type casted properly.
	@Override
	public Object clone(){
		try{
			return super.clone();
		}
		catch(CloneNotSupportedException e){
			return null;
		}
	}
	@Override
	public String toString(){

		return "Name:"+firstName + " " + lastName +" Height: " + height+
		" inches. "+ "Weight:" + weight + " BMI:"+ bodyMassIndex;
	}
	//runs the calculation of BMI.
	public void calculateBodyMassIndex(double weight, int height){
		double BMI = ((weight/ (height * height)) * 703);
		this.bodyMassIndex = BMI;
	}
	//runs all 3 equations, useful for the derived class as two of
	//these methods are abstract.
	public void getStats(){
		this.calculateBodyMassIndex(weight, height);
		this.calculateBasalMetabolicRate();
		this.findEnergyExpenditureRequirement();
	}
	public abstract void calculateBasalMetabolicRate();
	public abstract void findEnergyExpenditureRequirement();
	


}
