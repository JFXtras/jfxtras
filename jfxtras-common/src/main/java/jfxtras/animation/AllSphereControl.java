
package allSphere;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Max Bisesi
 */
public class AllSphere extends Sphere{

    private SequentialTransition st = new SequentialTransition();
    private TranslateTransition moveAway = new TranslateTransition(Duration.millis(300),this);
 
    //AllSphereType type;
    public enum AllSphereType{
        RECORDADDER, EYECANDY, UPDATER;
    };
    
	// Contstructor with default color cyan
	
    public AllSphere(double radius){
        super(radius);
        setMaterial(new PhongMaterial( Color.CYAN) );
        setCursor(Cursor.HAND);
        floatAnimation();
        setClickBehavior(AllSphereType.RECORDADDER);
        setLayoutX(100);
        setLayoutY(100);

        moveAway.setOnFinished(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e){ 
                moveAway.jumpTo(Duration.ZERO);
            }
        });
           
    }
	
	//Constructor to choose color based on phongmaterial
    public AllSphere(double radius, PhongMaterial mat){
        super(radius);
        setMaterial(mat);
        setCursor(Cursor.HAND);
        floatAnimation();
		
		moveAway.setOnFinished(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e){ 
                moveAway.jumpTo(Duration.ZERO);
            }
        });
    }
 
    public void floatAnimation(){
        
        //add sphere animations
        KeyValue kv = new KeyValue(translateYProperty(),20);
        Timeline animation = new Timeline();
        animation.getKeyFrames().add(new KeyFrame(Duration.millis(3000),kv));
 
        KeyValue kv4 = new KeyValue(translateYProperty(),5); 
        Timeline animation2 = new Timeline();
        animation2.getKeyFrames().add(new KeyFrame(Duration.millis(1500),kv4));
        
        KeyValue kv5 = new KeyValue(translateYProperty(),10);
        Timeline animation3 = new Timeline();
        animation3.getKeyFrames().add(new KeyFrame(Duration.millis(2000),kv5));
       
        st.getChildren().addAll(animation,animation2,animation3);
        st.setCycleCount(Timeline.INDEFINITE);
        st.setAutoReverse(true);
        st.play();
    }
    
    public void moveAway(){ 
        st.jumpTo(Duration.ZERO);
        moveAway.setByX(800);
        moveAway.setCycleCount(1);
        moveAway.play();  
    }
    
	//decide what happens after chosen animation
    public void setFinishOperation(EventHandler<ActionEvent> ea){
        moveAway.setOnFinished(ea);
    }
    
	//Decide what happens when the spere is clicked
    public void setClickBehavior(AllSphereType x){
        
        if(x.equals(AllSphereType.RECORDADDER)){
            setOnMouseClicked(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent e){
                    moveAway();
                }
            });
        }
        
        if(x.equals(AllSphereType.EYECANDY)){
			//implement behavior based on type of sphere
            
        }
        
        if(x.equals(AllSphereType.UPDATER)){
            //implement behavior based on type of sphere
        }
        
    }
}
