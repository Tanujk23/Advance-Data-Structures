import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

public class gatorTaxi {

    public static void main(String[] args) {
        RedBlackTree rideRequestsTree = new RedBlackTree(); // Creates an instance of RedBlackTree
        MinHeapp rideRequestsHeap = new MinHeapp(2000); // Creates an instance of MinHeap,
        String InputTxt = args[0]; // Reads the input file name from the command line arguments.

        try (BufferedReader reader = new BufferedReader(new FileReader(InputTxt))) {
            // Opens the input file for reading using BufferedReader to efficiently read
            // lines from the file.

            FileWriter writeToFile = new FileWriter("output_file.txt");
            String InputL = reader.readLine();

            while (InputL != null) {

                String[] inpOp = InputL.split("\\("); // Splits the input line into an array of strings at the "("character.
                String operation = inpOp[0].trim().toLowerCase(); // Extracts the operation from the input line, removes
                                                                  // leading/trailing whitespaces, and converts to
                                                                  // lowercase for case-insensitive comparison.
                String[] params = inpOp[1].trim().replaceAll("\\)", "").split(",");
                int rideId, rideDuration, rideCost;

                // Checks if the operation is "insert".
                if (operation.equals("insert")) {
                    rideId = Integer.valueOf(params[0].trim());
                    rideDuration = Integer.valueOf(params[1].trim());
                    rideCost = Integer.valueOf(params[2].trim());
                    Ride temp = rideRequestsTree.searchRide(rideId);
                    if (temp != rideRequestsTree.getBLACKNULL()) {
                        writeToFile.write("Duplicate RideNumber"); // Gives an error message if the rideId already exist
                                                                   // and stops writing to output file.
                        writeToFile.close();
                        System.exit(0);
                    } else {
                        Ride insertedNode = rideRequestsTree.insertRide(rideId, rideDuration, rideCost);
                        if (!rideRequestsHeap.insertRide(insertedNode))
                            writeToFile.write("Heap Overflow: Could not insertRide"); // In case of heap overflow
                    }
                } // Checks if the operation is "updatetrip".
                else if (operation.equals("updatetrip")) {
                    rideId = Integer.valueOf(params[0].trim());
                    int newduration = Integer.valueOf(params[1].trim());
                    Ride temp = rideRequestsTree.searchRide(rideId); // Searchs for id in redblacktree
                    int Ride = rideId, newCost = temp.cost + 10, newTripDuration = newduration;
                    int oldTripDuration = temp.duration; // updates the trip cost based on the new trip duration
                    if (oldTripDuration >= newTripDuration) {
                        temp.duration = newTripDuration;
                        rideRequestsHeap.bubbleUp(temp.index_in_heap);
                    } else {
                        rideRequestsHeap.deleteRide(temp.index_in_heap);
                        rideRequestsTree.deleteRide(Ride);
                        if (2 * oldTripDuration >= newTripDuration) {
                            Ride newNode = rideRequestsTree.insertRide(Ride, newCost, newTripDuration);
                            rideRequestsHeap.insertRide(newNode);
                        }
                    }
                } // Checks if the operation is "print".
                else if (operation.equals("print")) {
                    rideId = Integer.valueOf(params[0].trim());
                    int l = params.length;
                    // Gives the Ride information if it has only one argument, returns (0,0,0) if doesnot exist
                    if (l == 1) {
                        Ride temp = rideRequestsTree.searchRide(rideId);
                        if (temp != rideRequestsTree.getBLACKNULL()) {
                            writeToFile.write("(" + temp.Id + "," + temp.cost + "," + temp.duration + ")");
                        } else {
                            writeToFile.write("(0,0,0)");
                        }
                    } // Gives all the Ride details in Range of the given Ride Id's
                    else if (l == 2) {
                        int ride1 = Integer.valueOf(params[0].trim());
                        int ride2 = Integer.valueOf(params[1].trim());
                        String s = rideRequestsTree.searchRide(ride1, ride2);
                        int n = s.length();
                        if (n != 0) {
                            writeToFile.write(s.substring(0, n - 1));
                        } else {
                            writeToFile.write("(0,0,0)");
                        }
                    }
                } // Checks if the operation is "getnextride".
                else if (operation.equals("getnextride")) {
                    int del_ride = rideRequestsHeap.extractMin(); // gets the ride with least rideDuration from minHeap
                    if (del_ride != -1) {
                        Ride temp = rideRequestsTree.searchRide(del_ride);
                        writeToFile.write("(" + temp.Id + "," + temp.cost + "," + temp.duration + ")");
                        rideRequestsTree.deleteRide(del_ride); // deleted the ride from the RedBlackTree
                    } else {
                        writeToFile.write("No active ride requests"); // if there are no rides present
                    }
                } // Checks if the operation is "cancelride".
                else if (operation.equals("cancelride")) {
                    rideId = Integer.valueOf(params[0].trim());
                    Ride temp2 = rideRequestsTree.searchRide(rideId);
                    // Search based on the Id and delete from both MinHeap and ReadBlackTree
                    if (temp2 != rideRequestsTree.getBLACKNULL()) {
                        rideRequestsHeap.deleteRide(temp2.index_in_heap);
                        rideRequestsTree.deleteRide(rideId);
                    }
                } else {
                    System.out.println("input operation not recognized: " + operation);
                }

                InputL = reader.readLine();
                if (InputL != null) {
                    if (operation.equals("getnextride") || operation.equals("print"))
                        writeToFile.write("\n");
                }
            }
            writeToFile.close();
        } // If error is occured while reading file
        catch (IOException e) {
            System.out.println("input file Read Error: " + e.getMessage());
        }
    }
}