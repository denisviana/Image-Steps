# Image-Steps
A simple library for using steps with images and animation

![Alt Text](https://media.giphy.com/media/3kJQDzduX5CMXGv3CG/giphy.gif)

## Instalation

- Dependencie <br/>
``
compile 'com.github.denisviana:Image-Steps:1.0.0'
``
<br/><br/>
- xml <br/>
``````
<io.github.denisviana.imagestep.ImageSteps
        android:background="#913D88"
        android:id="@+id/imageSteps"
        android:layout_width="match_parent"
        android:layout_height="150dp"
        app:default_color="#fff"/>
``````
- kotlin <br/>
Add the image resource ids. The number of ids added represents the number of steps in the view. The order of the IDs added, represents the order of the steps in View
````
imageSteps.addSteps(
  R.drawable.ic_welcome,
  R.drawable.icon_users,
  R.drawable.ic_check)
````
  <t/>The view contains two methods for moving the steps <br/>
``
imageSteps.nextStep() 
``
<br/>
``
imageSteps.previousStep() 
``

## Authors

* **Denis Viana** -  [denisviana](https://github.com/denisviana)
