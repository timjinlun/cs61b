public class Dog {
    /** One integer constructor for dogs.*/
    public int weight;
    public Dog(int w){
        weight = w;
    }
    public void MakeNoise(){
        if (weight < 10){
            System.out.println("wo");
        } else if (weight < 30) {
            System.out.println("wooooo");

        }else {
            System.out.println("wwwooooo");
        }
    }
}
