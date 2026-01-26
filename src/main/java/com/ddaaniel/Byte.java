package com.ddaaniel;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

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
	private static final Logger log = Logger.getLogger(Byte.class.getName());

	public Result<String, String> tryUnionType(int code){
		if (code > 100000) return new Result.Success<>("OPERATIONS A SUCCESS!");
		return new Result.Failure<>("OPERATIONS WAS A CATASTROPHE!");
	}

	public void donothing() {
		log.info("nothing");
	}
}



public class Byte {
	private static final Logger log = Logger.getLogger(Byte.class.getName());

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
	public void doReflection() {
		try {
			Object typeReference = Class.forName("com.ddaaniel.Oh").getDeclaredConstructor().newInstance();
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

	public static void main(String[] args) {

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
			new Byte().doReflection();

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

		ArrayList<Integer> list = new ArrayList<>();
		list.stream().filter(element -> (element > 10));
	}
}
