import java.util.ArrayList;

public class Version {
	ArrayList<Integer> intList = new ArrayList<Integer>();
	public enum compState{SELF, OTHER, SAME}; //comparison state
	public String versionString = "";
	
	
	public Version(String versionString){
		parseVersion(versionString);
	}
	
	public void parseVersion(String inString){
		try{
		if(inString.contains(".")){
			// there is more than one integer
			int occurances = countOccurrencesOf(inString, '.');
			for(int i = 0; i < occurances; i++){
				int nextOccurance = inString.indexOf('.');
				String number = inString.substring(0, nextOccurance);
				versionString += inString.substring(0, nextOccurance);
				intList.add(Integer.parseInt(number));
				inString = inString.substring(nextOccurance +1 );
				if(i < occurances-1){
					versionString+=".";
				}
			}
			intList.add(Integer.parseInt(inString));
		}
		else { // otherwise its just one number
			intList.add(Integer.parseInt(inString));
		}
		}
		catch(NumberFormatException e){
			intList.add(0);
		}
	}

	public String getVersion(){
		return versionString;
	}
	
	public compState getBiggerVersion(Version otherVersion){
		int smallestlayers = min(intList.size(), otherVersion.getSize());
		for(int i = 0; i < smallestlayers; i++){
			if(intList.get(i) < otherVersion.getIntAt(i)){
				// this list is the smaller one
				return compState.OTHER;
			}
			else if (intList.get(i) > otherVersion.getIntAt(i)){
				// other version is bigger
				return compState.SELF;
			}
			// otherwise go to the next iteration
		}
		// if they are both the same till now it is whichever has more spaces
		if (intList.size() < otherVersion.getSize()){
			return compState.OTHER;
		}
		else if (intList.size() > otherVersion.getSize()){
			return compState.SELF;
		}
		return compState.SAME;
	}
	
	public boolean isBiggerVersion(Version otherVersion){
		compState state = getBiggerVersion(otherVersion);
		if (state == compState.SELF){
			return true;
		}
		return false;
	}
	
	
	private int min(int size, int size2) {
		if (size < size2)
			return size;
		return size2;
	}

	private int getIntAt(int position){
		return intList.get(position);
	}
	
	public int getSize(){
		return intList.size();
	}
	
	public int countOccurrencesOf(String toSearch, char searchFor){
		int occurances = 0;
		
		for(int i = 0; i < toSearch.lastIndexOf(searchFor)+1; i++){
			if(toSearch.charAt(i) == '.'){
				occurances++;
				
			}
		}
		
		return occurances;
	}
	
	
}
