
public class Mod {
	Version webVer;
	Version localVer;
	
	String webPath;
	String localPath;
	
	String name;
	
	enum VersionType{LOCAL, WEB};
	VersionType isBigger;
	
	public Mod(Version NlocalVer, Version NwebVer, String NlocalPath, String NwebPath, String Nname){
		webVer = NwebVer;
		localVer = NlocalVer;
		
		webPath = NwebPath;
		localPath = NlocalPath;
		
		isBigger = getIsBigger();
		
		name = Nname;
		
	}
	public Mod(String NwebPath, String Nname, Version NwebVer, String NlocalPath){
		webPath = NwebPath;
		localPath = NlocalPath;
		isBigger = VersionType.WEB;
		webVer = NwebVer;
		name = Nname;
	}

	private VersionType getIsBigger(){
		if (webVer.getBiggerVersion(localVer) == Version.compState.SELF)
			return VersionType.LOCAL;
		return VersionType.WEB;
	}
	

}
