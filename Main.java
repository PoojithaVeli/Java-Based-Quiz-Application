package Project;

import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		        Scanner sc = new Scanner(System.in);
		        
		        System.out.println("=================================================");
		        System.out.println("\u001B[36m         Welcome to the Java Quiz Application!       \u001B[0m");
		        System.out.println("=================================================");
		        
		        System.out.println("Please enter your details:");
		        System.out.print("Enter your name: ");
		        String name = sc.nextLine();
		        System.out.print("Enter your city: ");
		        String city = sc.nextLine();
		        System.out.print("Enter your age: ");
		        int age = sc.nextInt();
		        
		        Player player = new Player(name, city, age);
		        
		        GameController gameController = new GameController(player);
		        gameController.startGame();
		        
		        sc.close();
		    }
		}


	


