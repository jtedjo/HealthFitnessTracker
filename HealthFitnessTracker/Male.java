import java.util.ArrayList;

public class Male extends UserInfo{
	public static final int DEFAULT_BASAL_METABOLIC_RATE = 1;
	public static final String DEFAULT_SEDENTARY = "Sedentary";
	public static final String DEFAULT_MODERATE = "Moderate";
	public static final String DEFAULT_ACTIVE = "Active";
	public static final String DEFAULT_VERY_ACTIVE = "Very active";
	public static final double DEFAULT_ENERGY_EXPEND = 0;
	
	
	private double basalMetabolicRate;
	private String activityLevel;
	private double energyExpenditureRequirement;
	
	public Male(String firstName, String lastName, int height, 
		double weight, int age, String gender, double bodyMassIndex, 
		double basalMetabolicRate, String activityLevel, 
		double energyExpenditureRequirement){
			
		super(firstName,lastName,height,weight,age, gender);
		setAll(basalMetabolicRate, activityLevel, energyExpenditureRequirement);
	}
	public Male(double basalMetabolicRate, String activityLevel, 
		double energyExpenditureRequirement){
			
		super();
		setAll(basalMetabolicRate, activityLevel, energyExpenditureRequirement);
	}
	public Male(){
		super();
		basalMetabolicRate = DEFAULT_BASAL_METABOLIC_RATE;
		activityLevel = DEFAULT_SEDENTARY;
		energyExpenditureRequirement = DEFAULT_ENERGY_EXPEND;
	}
	public boolean setAll(double basalMetabolicRate, String activityLevel, double energyExpenditureRequirement){
		if (setBasalRate(basalMetabolicRate) &&
		 setActivityLevel(activityLevel) && 
		 setEnergyExpenditureRequirement(energyExpenditureRequirement)){
			return true;
		 }
		 else{
			 System.out.println("set all did not work");
			 return false;
		 }
	}
	public boolean setBasalRate(double basalRate){
		if (basalRate > 0){
			basalMetabolicRate = basalRate;
			return true;
		}
		else{
			System.out.println("Basal rate has to be higher than 0!");
			return false;
		}
	}
	public boolean setActivityLevel(String activityLevel){
		String choices = "" + DEFAULT_SEDENTARY+ DEFAULT_ACTIVE +
							DEFAULT_MODERATE + DEFAULT_VERY_ACTIVE; 
		if (choices.contains(activityLevel)){
			this.activityLevel = activityLevel;
			return true;
		}
		else{
			System.out.println("Activity level is not set correctly!");
			return false;
		}
	}
	public double getBasalRate(){
		return basalMetabolicRate;
	}
	public String getActivityLevel(){
		return activityLevel;
	}
	public double getEnergyExpenditureRequirement(){
		return energyExpenditureRequirement;
	}
	
	public boolean setEnergyExpenditureRequirement(double energyExpend){
		if (energyExpend > 0){
			this.energyExpenditureRequirement = energyExpend;
			return true;
		}
		else{
			return false;
		}
	}
	public void calculateBasalMetabolicRate(){
		double weightInKG = convertPoundToKilo(this.getWeight());
		double heightInCM = convertInchesToMeter(getHeight()) * 100;
		basalMetabolicRate = ((10 * weightInKG) + (6.25 * heightInCM) 
			- (5* getAge())+ 5);
	}
	//adult vs kids difference
	public void findEnergyExpenditureRequirement(){
		double weightInKG = convertPoundToKilo(this.getWeight());
		double heightInM = convertInchesToMeter(getHeight());
		double physicalCoefficient = this.calculatePhysicalCoefficient();
		if (activityLevel == null)
		{
			System.out.println("Physical activity level was not set!");
		}
		else if (this.getAge() > 18){
			
			energyExpenditureRequirement = ((662 - 
			(9.53 * this.getAge()))+physicalCoefficient 
			* (15.91 * weightInKG) + (539.6 * heightInM));
		}
		else if (this.getAge() < 18){
			
			energyExpenditureRequirement = ((88.5 - 
			(61.9 * this.getAge()))+physicalCoefficient 
			* (26.7 * weightInKG) + (903 * heightInM));
		}
	}
	public double calculatePhysicalCoefficient(){
		double physicalCoefficient = 0;
		if (activityLevel != null && this.getAge() > 18){
			switch(activityLevel){
				case DEFAULT_SEDENTARY: physicalCoefficient =1;
					break;
				case DEFAULT_MODERATE: physicalCoefficient =1.11;
					break;
				case DEFAULT_ACTIVE: physicalCoefficient =1.25;
					break;
				case DEFAULT_VERY_ACTIVE: physicalCoefficient =1.48;
					break;
			}
			return physicalCoefficient;
		}
		else if (activityLevel != null && this.getAge() < 18){
			switch(activityLevel){
				case DEFAULT_SEDENTARY: physicalCoefficient =1;
					break;
				case DEFAULT_MODERATE: physicalCoefficient =1.13;
					break;
				case DEFAULT_ACTIVE: physicalCoefficient =1.26;
					break;
				case DEFAULT_VERY_ACTIVE: physicalCoefficient =1.42;
					break;
			}
			return physicalCoefficient;
		}
		else{
			System.out.println("Need to set activity level!");
			return -1;
		}
	 }
	 //in grams
	 public String recommendedProtein(){
		 String protein = null;
		 double proteinIntake = energyExpenditureRequirement * .8;
		 protein = protein.format("Protein:%.2fgrams", proteinIntake);
		 return protein;
	 }
	  public String recommendedFat(){
		 String fat = null;
		 double minimumFat = energyExpenditureRequirement * .20/ 9;
		 double maximumFat = energyExpenditureRequirement * .35 / 9;
		fat = fat.format("Fat:%.2fgrams-%.2fgrams.", minimumFat, maximumFat);
		return fat;
	 }
	 public String recommendedCarbohydrate(){
		 String carbs = null;
		 double minimumCarbs = energyExpenditureRequirement * .45/ 9;
		 double maximumCarbs = energyExpenditureRequirement * .65 / 9;
		 carbs = carbs.format("Carbs:%.2fgrams-%.2fgrams.", minimumCarbs, maximumCarbs);
		 return carbs;
	 }
}
