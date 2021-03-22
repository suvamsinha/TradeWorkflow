import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Questions {

	public static void main(String[] args) {
		
		
		List<String> list1 = Arrays.asList("M","W","J","K","T");
		List<String> list2 = Arrays.asList("M","W","E","K","T");
		//output = {M,W,K,T}
		List<String> result = doFilter(list1,list2);
		
		for(String res : result) {
			System.out.print(" "+res);
		}

	}

	public static List<String> doFilter(List<String> item1, List<String> item2){

		Set<String> itemSet = new HashSet<String>();
		List<String> result = new ArrayList<String>();

		for(String s : item1){
		itemSet.add(s);
		}

		for(String s : item2){

		if(!itemSet.add(s)){
		result.add(s);
		}

		}
		return result;


		}
}
