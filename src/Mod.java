
public class Mod {
	Version webVer;
	Version localVer;
	
	String webPath;
	String localPath;
	
	String name;
	
	public Mod(Version NlocalVer, Version NwebVer, String NlocalPath, String NwebPath, String Nname){
		webVer = NwebVer;
		localVer = NlocalVer;
		
		webPath = NwebPath;
		localPath = NlocalPath;
		
		name = Nname;
	}

	

}
