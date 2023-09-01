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
    public static Dog maxDog(Dog d1, Dog d2){
        if (d1.weight > d2.weight){
            return d1;

        }return d2;

        }

    }
