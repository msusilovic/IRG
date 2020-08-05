package peti;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		String name = args[0];
		ObjectModel model = new ObjectModel();

		try (BufferedReader br = new BufferedReader(new FileReader(name))) {

			List<Vertex3D> vertexes = new ArrayList<>();
			List<Face3D> faces = new ArrayList<>();
			model.setFaces(faces);
			model.setVertexes(vertexes);
			String line = br.readLine();
			while(line != null) {
				if(line.startsWith("v")) {
					String[] parts = line.trim().split("\\s+");
					Vertex3D v = new Vertex3D(Double.parseDouble(parts[1]), 
											  Double.parseDouble(parts[2]), 
											  Double.parseDouble(parts[3]));
					vertexes.add(v);
					
				}else if (line.startsWith("f")) {
					String[] parts = line.trim().split("\\s+");
					Face3D f = new Face3D(Integer.parseInt(parts[1])-1, 
										  Integer.parseInt(parts[2])-1,
										  Integer.parseInt(parts[3])-1);
					model.addFace3D(f);
				}
				line = br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		try (Scanner sc = new Scanner(System.in)){
			System.out.println("Unesite naredbu \"normiraj\" \"tocka x y z\" ili \"quit\".");
			String line = sc.nextLine().strip();
			while(line != null) {
				
				if (line.equals("quit")) {
					return;
					
				} else if (line.equals("normiraj")) {
					ObjectModel copy = model.copy();
					copy.normalize();
					System.out.println(copy.dumpToOBJ());
					
				}else if (line.toLowerCase().startsWith("tocka")) {
					String[] parts = line.split("\\s+");
					try {
						double x = Double.parseDouble(parts[1]);
						double y = Double.parseDouble(parts[2]);
						double z = Double.parseDouble(parts[3]);
						int status = model.pointStatus(x, y, z);
						if (status < 0) {
							System.out.println("Točka je unutar tijela.");
						}else if (status > 0) {
							System.out.println("Točka je izvan tijela");
						}else {
							System.out.println("Točka je na oplošju tijela.");
						}
						
					}catch (NumberFormatException | IndexOutOfBoundsException e) {
						System.out.println("Krivi format");
					}			
				}else {
					System.out.println("Nepoznata naredba.");
				}
				line = sc.nextLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
