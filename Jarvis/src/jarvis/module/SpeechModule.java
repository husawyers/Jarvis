/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jarvis.module;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

/**
 *
 * @author Hebron
 */
public class SpeechModule extends Module {
    private Voice voice;
    
    public SpeechModule() {
        System.out.println("Initialising speech module...");
        
        VoiceManager manager = VoiceManager.getInstance();
        voice = manager.getVoice("kevin");
        voice.allocate();
        
        System.out.println("done");
    }
    
    @Override
    public void shutdown() {
        System.out.println("Shutting down speech module...");
        
        voice.deallocate();
        
        System.out.println("done");
    }
    
    public void speak(String what) {
        // Error check
        if(what == null) return;
        
        voice.speak(what);
    }
}
