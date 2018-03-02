[![Android Arsenal]( https://img.shields.io/badge/Android%20Arsenal-Image%20Steps-green.svg?style=flat )]( https://android-arsenal.com/details/1/6788 )

# Image-Steps
A simple library for using steps with images and animation

![Alt Text](https://media.giphy.com/media/3kJQDzduX5CMXGv3CG/giphy.gif)

## Instalation

- Dependencie <br/>
``
compile 'com.github.denisviana:Image-Steps:1.0.4'
``
<br/><br/>
- **Xml** <br/>
``````
<io.github.denisviana.imagestep.ImageSteps
        android:background="#913D88"
        android:id="@+id/imageSteps"
        android:layout_width="match_parent"
        android:layout_height="150dp"
        app:default_color="#fff"/>
``````
- **Kotlin** <br/>
Add the image resource ids. The number of ids added represents the number of steps in the view. The order of the IDs added, represents the order of the steps in View
````
imageSteps.setSteps(
  R.drawable.ic_welcome,
  R.drawable.icon_users,
  R.drawable.ic_check)
````
  <t/>The view contains two methods for moving the steps <br/><br/>
``
imageSteps.next()
``
<br/>
``
imageSteps.previous()
``

Or can be used with ViewPager. In this case, it not necessary the use of the methods `next()` and `previous()`

- **With ViewPager**

`
imageSteps.setupWithViewPager(viewpager)
`

You can add a listener for listen the page changes in viewpager
````
 imageSteps.setOnViewPagerChangeListener(object : ImageSteps.OnViewPagerChangeListener {
             override fun onViewPagerPageScrollStateChanged(state: Int) {
             }
 
             override fun onViewPagerPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
             }
 
             override fun onViewPagerPageSelected(position: Int) {
                 handlePageChanged(position)
             }
 
         })
````


## Author

* **Denis Viana** -  [denisviana](https://github.com/denisviana)

## Video tutorial
https://youtu.be/OA_NNqhVxVY 


