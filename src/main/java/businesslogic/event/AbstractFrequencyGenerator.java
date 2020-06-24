package businesslogic.event;

import java.time.LocalDate;

public abstract class  AbstractFrequencyGenerator implements FrequencyGenerator{
   private int baseFrequency;

   AbstractFrequencyGenerator(int baseFrequency){
      this.baseFrequency = baseFrequency;
   }
}
