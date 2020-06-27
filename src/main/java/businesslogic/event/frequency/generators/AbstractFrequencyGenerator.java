package businesslogic.event.frequency.generators;

public abstract class  AbstractFrequencyGenerator implements FrequencyGenerator{
   private int baseFrequency;

   AbstractFrequencyGenerator(int baseFrequency){
      this.baseFrequency = baseFrequency;
   }
}
