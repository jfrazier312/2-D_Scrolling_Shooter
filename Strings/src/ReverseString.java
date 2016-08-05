import java.util.Arrays;

public class ReverseString {
	
	
	public static void main(String[] args){
		ReverseString a = new ReverseString();
		
		a.reverseAndPrint2("localRepoWorkspace");	
	}
	
	public void reverseAndPrint(String str){
		StringBuilder build = new StringBuilder();
		build.append(str);
		build.reverse();
		System.out.println(build.toString());
	}
	
	public void reverseAndPrint2(String str){
		char[] arr = new char[str.length()];
//		for(int j = 0, i = str.length()-1; i >= 0; i--, j++){
//			arr[j] = str.charAt(i);
//		}
		for(int i = 0; i < str.length(); i++){
			arr[i] = str.charAt(str.length()-1-i);
		}
		System.out.println(Arrays.toString(arr));
	}

}
