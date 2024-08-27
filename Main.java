import java.util.Scanner;
import java.util.ArrayList; 

class CarNotAvailable extends Exception
{
    CarNotAvailable(String s)
    {
        System.out.println(s);
    }
}

class RentalNotFound extends Exception

{
    RentalNotFound(String s)
    {
        System.out.println(s);
    }
}

class Car
{
    private String carId;
    private String brand;
    private String name;
    private double basePricePerDay;
    private boolean isAvailable;

    Car(String carId,String brand,String name,double basePricePerDay)
    {
        this.carId = carId;
        this.brand = brand;
        this.name = name;
        this.basePricePerDay = basePricePerDay;
        this.isAvailable = true;
    }
    String getId()
    {
        return carId;
    }
    String getBrand()
    {
        return brand;
    }
    String getName()
    {
        return name;
    }
    double getPrice()
    {
        return basePricePerDay;
    }
    boolean getAvailable()
    {
        return isAvailable;
    }
    double calculatePrice(int rentalDays)
    {
        return basePricePerDay * rentalDays;
    }
    void rent()
    {
        isAvailable = false;
    }
    void returnCar()
    {
        isAvailable = true;
    }
}


class Rental
{
    private Car car;
    private Customer customer;
    private int days;

    Rental(Car car,Customer customer,int days)
    {
        this.car = car;
        this.customer = customer;
        this.days = days;   
    }
    Car getCar()
    {
        return car;
    }

    Customer getCustomer()
    {
        return customer;
    }
    int getDays()
    {
        return days;
    }
}

class Customer

{
    private String name;
    private String cust_ID;

    Customer(String name,String id)
    {
        this.name = name;
        this.cust_ID = id;
    }

    String getName()
    {
        return name;
    }
    String getId()
    {
        return cust_ID;
    }

}

class CarRentalSystem

{
    private ArrayList<Car> cars;
    private ArrayList<Customer> customers;
    private ArrayList<Rental> rentals;

    CarRentalSystem()
    {
        cars = new ArrayList<>();
        customers = new ArrayList<>();
        rentals = new ArrayList<>();
    }

    void addCar(Car car)
    {
        cars.add(car);
    }

    void addCustomer(Customer customer)
    {
        customers.add(customer);
    }

    void addRental(Rental rental)
    {
        rentals.add(rental);
    }

    public void rentCar(Car car,Customer customer,int days) throws CarNotAvailable
    {
        if(!car.getAvailable()) 
            throw new CarNotAvailable("Sorry, Car is Not Available!!");
        
        car.rent();
        rentals.add(new Rental(car,customer,days));
        System.out.println("Car Rented Successfully!");
    }

    public void returnCar(Car car) throws RentalNotFound
    {
        Rental rentalToRemove = null;
        for(Rental rental : rentals)
        {
            if(rental.getCar() == car)
            {
                rentalToRemove = rental;
                break;
            }
        }
        if(rentalToRemove == null)
            throw new RentalNotFound("No such Rental!!");
        
        rentals.remove(rentalToRemove);
        car.returnCar();

        System.out.println("Car Returned Successfully");
    }


    private Car checkCar(String id)
    {
        for(Car car : cars)
        {
            if(car.getId().equals(id) && car.getAvailable())
                return car;
        }
        return null;
    }

    private Car carReturn(String id)
    {
        for(Car car : cars)
        {
            if(car.getId().equals(id) && !car.getAvailable())
                return car;
        }
        return null;
    }

    private void getReceipt(Car car,Customer customer,int days)
    {
        System.out.println("\n----- Rental Information ----- \n");
        System.out.println("Customer Id : " + customer.getId());
        System.out.println("Customer Name : " + customer.getName());
        System.out.println("Car : " + car.getName() + " " + car.getBrand());
        System.out.println("Rental Days : " + days);
        System.out.println("Total Price : " + days * car.getPrice());
    }

    public void menu() throws CarNotAvailable,RentalNotFound
    {
        Scanner sc = new Scanner(System.in);
        while(true)
        {
            System.out.println("\n----- Car Rental System -----\n");
            System.out.println("Click : ");
            System.out.println("[1].Rent a Car\t[2].Return a car\t[3].Exit");
            int choice = sc.nextInt();

            sc.nextLine(); //consume new line

            switch (choice) 
            {
                case 1:
                    System.out.println("\n----- Rent a car ------\n");
                    
                    System.out.println("\n----- Available Cars ------\n");
                    for(Car car : cars)
                    {
                        if(car.getAvailable())
                            System.out.println(car.getId() + " " + car.getBrand() + " " + car.getName());
                    }
                    System.out.print("\nEnter your name : ");
                    String name = sc.nextLine();
                    System.out.print("\nEnter the car Id you want to rent : ");
                    String id = sc.nextLine();

                    System.out.print("Enter for how many days you want to rent a car : ");
                    int days = sc.nextInt();
                    sc.nextLine();

                    Customer c = new Customer(name, "CUS" + customers.size());
                    addCustomer(c);

                    Car car = checkCar(id);
                    if(car == null)
                        throw new CarNotAvailable("Sorry, Car is Not Available!");
                    
                    getReceipt(car,c,days);

                    System.out.println("\nConfirm Rental : (Y/N) : ");
                    char ch = sc.next().charAt(0);
                    if(ch != 'Y' && ch != 'y')
                        throw new RentalNotFound("Rental Canceled!");
                    rentCar(car,c,days);
                    System.out.println("\nCar Rented Succesfully!");
                
                break;
                case 2:
                    System.out.println("\n----- Return a Car -----\n");
                    System.out.print("Enter the car Id you want to return : ");
                    String carId = sc.nextLine();

                    car = carReturn(carId);
                    if(car == null)
                        throw new CarNotAvailable("Car Not Found!!");
                    returnCar(car);
                    System.out.println("\nCar Returned Successfully!");
                break;
                case 3:
                    sc.close();
                    System.exit(0);
                break;
                default:
                    System.out.println("Please select any valid service!!");
            }
        }
    }

}

public class Main 
{
    public static void main(String[] args)
    {
        CarRentalSystem r = new CarRentalSystem();
        Car car1 = new Car("C001","Toyota","Camry",60);
        Car car2 = new Car("C002","Honda","Accord",70);
        Car car3 = new Car("C003","Mahindra","Thar",150);

        r.addCar(car1);
        r.addCar(car2);
        r.addCar(car3);

        try
        {
            r.menu();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }    
}