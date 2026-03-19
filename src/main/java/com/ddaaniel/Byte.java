package com.ddaaniel;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;
import java.util.random.RandomGenerator;
import java.util.stream.Stream;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;

sealed interface Result<T, E> {
	record Success<T, E>(T value) implements Result<T, E> {
	}

	record Failure<T, E>(E error) implements Result<T, E> {
	}
}

class Oh {
	private static final Logger log = Logger.getLogger(Oh.class.getName());

	public Result<String, Integer> tryUnionType(boolean reason) {
		if (reason)
			return new Result.Success<>("OPERATIONS A SUCCESS!");
		return new Result.Failure<>(500);
	}

	public void ok() {
		// var resultado = tryUnionType(true);
		// var x = switch (resultado) {

		switch (tryUnionType(true)) {
			case Result.Success(var s) -> System.out.println(s);
			case Result.Failure(var f) -> log.info("Error: " + f);
		}
		;

	}

	/*
	 *
	 * [.java ] → [Compilador javac] → [Bytecode .class] →
	 * [JVM Carrega] → [Metadados na Memória] → [Reflection
	 * acessa]
	 *
	 * Metadados refere-se principalmente às informações sobre as
	 * classes (como definições, métodos, constantes) armazenadas
	 * na área de memória chamada Metaspace (ou PermGen antes do
	 * Java 8), essenciais para o carregamento, verificação e
	 * execução de classes, sendo vitais para o funcionamento interno
	 * da JVM e para recursos como reflexão.
	 *
	 */
	public void aboutReflection() {
		try {
			Object typeReference = Class
					.forName("com.ddaaniel.Oh")
					.getDeclaredConstructor()
					.newInstance();
			for (Method m : typeReference.getClass().getMethods()) {
				// var modifier = m.getModifiers();
				var returnType = m.getReturnType();
				var accessFlag = m.accessFlags();
				var parameters = m.getParameters();

				log.info(String.format("%s %s %s Params: %s",
						accessFlag,
						returnType.getSimpleName(),
						m.getName(),
						Arrays.toString(parameters)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void dealWithByteBuddy() {
		// bytecode manipulation (com ByteBuddy)
		// Modifica o bytecode ANTES da classe ser carregada
		Class<?> byteModel = new ByteBuddy()
				.subclass(Object.class)
				.method(ElementMatchers.named("toString"))
				.intercept(FixedValue.value("Intercepted by ByteBuddy!"))
				.make()
				.load(Byte.class.getClassLoader())
				.getLoaded();

		try {

			Object instanceModel = byteModel.getConstructor().newInstance();
			log.info(instanceModel.toString());
			new Oh().aboutReflection();

		} catch (Exception e) {
			e.printStackTrace();
		}

		/*
		 *
		 * When we use the 'Object.getClass();', we got a reference to an
		 * common Object java.lang.Class subjected to Garbage Collector,
		 * which resides on the Heap, that points to Metaspace structure
		 * inside of JVM (HotSpot) which contains the structure calls
		 * 'InstanceKlass' (memória nativa/off-heap), and there are placed
		 * the real metadata's like: class names, methods, fields and
		 * bytecode. When requested via '.getName()' or '.toString()', the
		 * object on the Heap consults this structure 'InstanceKlass' on
		 * Metaspace to recovery the class name string.
		 *
		 */
		log.info(new Oh().getClass().toString());

		List<String> someList = List.of("Ana", "Pedro", "Maria");
		Stream<String> list = someList.stream()
				.filter(n -> n.startsWith("A"))
				.map(n -> n.toUpperCase());

		list.forEach(n -> System.out.println(n));

		ArrayList<Integer> listt = new ArrayList<>();
		listt.stream().filter((element) -> (element > 10));
	}
}

class Game {
	private int score;

	public String quest() {
		var A = new Game[5];

		for (int count = 0;;) {
			if (count == A.length)
				break;
			A[count] = new Game();
			count++;
		}

		var B = A.clone();
		A[4].score = 550;

		return String.valueOf(A[4] == B[4]);
	}
}

class IsNMultipleOfM {

	public boolean isMultiple(long n, long m) {
		return m != 0 && n % m == 0;
	}
}

class EvenCannotUseTheMultiplicationModulusOrDivisionOperators {
	public boolean isEven(int i) {
		if (i < 0)
			i = -i;

		for (;;) {
			if (i < 1)
				break;
			i = i - 2;
		}
		// return (i & 1) == 0;
		return i == 0;
	}
}

class SumOfAllTheOddPositiveIntegersLessThanOrEqualToN {

	public int recursive_sum(int i) {
		if (i < 0)
			i = -i;
		if (i == 0)
			return 0;

		return (i & 1) == 1 ? i + recursive_sum(i - 1) : recursive_sum(i - 1);
	}

	public int sum(int i) {
		int x = 0;
		if (i < 0)
			i = -i;
		for (;;) {
			if (i == 0)
				break;
			if ((i & 1) == 1)
				x = x + i;
			i = i - 1;
		}

		return x;
	}
}

class SumOfTheSquaresOfAllPositiveIntegersLessThanOrEqualToN {

	public int recursive_sum(int i) {
		if (i > 0)
			i = -i;
		if (i == 0)
			return 0;

		return (i * i) + recursive_sum(i - 1);
	}

	public int sum(int i) {
		int x = 0;
		if (i < 0)
			i = -i;
		for (int count = 0;;) {
			if (i == 0 || !(count <= i))
				break;
			x = x + (count * count);
			count++;
		}

		return x;
	}
}

class CountstheNumberOfVowelsInAGivenCharacterString {
	public int countVowels(String str) {
		int x = 0;
		String string = str.toLowerCase();
		for (int count = 0;;) {
			if (!(count < string.length()))
				break;
			if (string.charAt(count) == 'a'
					|| string.charAt(count) == 'e'
					|| string.charAt(count) == 'i'
					|| string.charAt(count) == 'o'
					|| string.charAt(count) == 'u')
				x = x + 1;
			count++;
		}

		return x;
	}

	public int recursiveCount(String str) {
		if (str.isEmpty())
			return 0;
		char c = Character.toLowerCase(str.charAt(0));
		int count = "aeiou".indexOf(c) != -1 ? 1 : 0;

		return count + recursiveCount(str.substring(1));
	}
}


class UsesAstringBuilderInstanceToRemoveAllThePunctuationFromAStringSStoringASentence {

	public String removePunctuation(String s) {
		if (s.isEmpty()) return s;
		StringBuilder sb = new StringBuilder();

    for (char c : s.toCharArray()) {
        if (Character.isLetter(c) || Character.isWhitespace(c)) sb.append(c);
    }

		// for (int index = 0;;) {
		// 	Character letter = s.charAt(index);
		// 	if( Character.isLetter(letter) || Character.isWhitespace(letter)
		// 			) sb.insert(sb.length(), letter);
		// 	if (index == s.length() - 1) break;
		// 	index++;
		// }
		return sb.toString();
	}
}

class ReverseAnIntegerArray {
	public ArrayList<Integer> reverseArray(ArrayList<Integer> arr) {
		var array = new ArrayList<Integer>();

		for (Integer element : arr) {
			array.addFirst(element);
		}
		
		return array;
	}

	public int[] Arr(int[] arr) {

		int[] resultArr = new int[arr.length];
		int index = arr.length - 1;

		for (int i : arr) {
			resultArr[index] = i;	
			index--;
		}

		return resultArr;
	}
}

// Write a pseudocode description of a method for finding the smallest and largest
// numbers in an array of integers.
class FindTheSmallestAndLargestNumberInAnArray {

	public int[] smallestAndLargestInt(int[] arr) {

		int smallest = Integer.MAX_VALUE;
		int largest = Integer.MIN_VALUE;

		int[] array = new int[2];
		for (int i : arr) {
			largest = i > largest ? i : largest; 
			smallest = i < smallest ? i : smallest;
		}
		array[0] = smallest;
		array[1] = largest;

		return array;
	}


	public int[] smallestAndLargestIntNewWay(int[] arr) {
    if (arr == null || arr.length == 0) return new int[0];

		int smallest = arr[0];
		int largest = arr[0];

		for (int index = 1; index < arr.length; index++) {
			if (arr[index] > largest) largest = arr[index];
			else if (arr[index] < smallest) smallest = arr[index];
		}

		int[] array = new int[2];
		array[0] = smallest;
		array[1] = largest;

		return array;
	}


	public int[] smallestAndLargestIntPerformanceWay(int[] arr) {
    if (arr == null || arr.length == 0) return new int[0];

    int min = arr[0];
    int max = arr[0];

    for (int i = 1; i < arr.length; i += 2) {
        
        if (i == arr.length - 1) {
            if (arr[i] > max) max = arr[i];
            else if (arr[i] < min) min = arr[i];
            break;
        }

        if (arr[i] > arr[i + 1]) {
            if (arr[i] > max) max = arr[i];         // 2ª comparação: maior do par vs max
            if (arr[i + 1] < min) min = arr[i + 1]; // 3ª comparação: menor do par vs min
        } else {
            if (arr[i + 1] > max) max = arr[i + 1]; // 2ª comparação: maior do par vs max
            if (arr[i] < min) min = arr[i];         // 3ª comparação: menor do par vs min
        }
    }

    return new int[]{min, max};
	}
}





class DeterminesIfThereIsAPairOfDistinctElementsOfTheArrayWhoseProductIsEven {
	public boolean theArrayProductIsEven(int[] arr) {
		// If x * y == Even Then x or y == Even
		if (arr.length < 2) return false;
		for ( int i : arr) {
			if (i % 2 == 0) return true;
		}
		return false;
	}
}



class EuclidianNorm {

	public double norm(int[] v, int p) {
		double sum = 0;

		for (int i : v) {
			if ( i < 0 ) i = -i;
			sum += Math.pow(i, p);
		}
		
		return Math.sqrt(sum);
	}

	public double norm(int[] v) {
		int p = 2;
		double sum = 0;

		for (int i : v) {
			if ( i < 0 ) i = -i;
			sum += Math.pow(i, p);
		}
		
		// p-ésima_raiz → ᵖ√soma == soma¹⸍ᵖ
		return Math.pow(sum, 1.0 / p);
	}
}

class TakeAPositiveIntegerGreaterThan2AsInputAndWriteOutTheNumberOfTimesOneMustRepeatedlyDivideThisNumberBy2BeforeGettingAValueLessThan2 {

	public int x(int y) {
		if (y <= 2) return 0;
		int count = 0;
		
		for (;;) {
			if (y < 2) break;
			y = y / 2;
			count++;
		}

		return count;
	}
}


class TakesAnArrayOfFloatValuesAndDeterminesIfAllTheNumbersAreDifferentFromEachOther { 

	public boolean x(float[] arr) {

		Set<Float> list = new HashSet<>();
		for ( float i : arr ) {
			if ( list.add(i) == false ) return false;
		}

		return true;
	}
}


class TakesAnArrayContainingTheSetOfAllIntegersInTheRange1To52AndShufflesItIntoRandomOrderCatchEachOrder {

	// swap-based: the main idea is to swap between the current index(last index 
	// dereased in -1 till the position be equal to 0) with a random numbers from
	// 0 to n( where decreases until it reaches 0 ). This is crucial because we 
	// have 4 distinct elements in specific positions and want to find all possible
	// permutations. While 4 positions could result in 4 * 4 * 4 * 4, possibilities 
	// if repetitions were allowed, our case involves distinct elements without 
	// repetition. However, since it is about possible combinations and without 
	// repeting the elements in each occurence, the possibility to each position 
	// would be -1 progressively (4!). Therefore, the available options for each 
	// position decrease progressively: 1st → 4; 2nd → 3; 3rd → 2; 4th → 1, 
	// resulting in (4 factorial). 
	public int[] fisherYatesShuffles(int[] arr) {
		Random random = new Random();

		for (int i = arr.length - 1; i > 0; i--) { 
			int j = random.nextInt(i + 1);          

			int temp = arr[j];
			arr[j] = arr[i];
			arr[i] = temp;
		}

		return arr;
	}
}




public class Byte {

	public static void main(String[] args) {
		System.out.println(String.valueOf(new IsNMultipleOfM().isMultiple(15, 3)));
	}
}
