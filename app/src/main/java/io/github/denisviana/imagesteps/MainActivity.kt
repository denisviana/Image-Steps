package io.github.denisviana.imagesteps

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.github.denisviana.imagestep.ImageSteps
import kotlinx.android.synthetic.main.main_activity.*

/**
 * Created by Denis Viana on 17/02/2018.
 * e-mail denisvcosta@gmail.com
 * github https://github.com/denisviana
 */
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        imageSteps.addSteps(R.drawable.ic_welcome,R.drawable.icon_users,R.drawable.ic_check)
        imageSteps.scaleUp = 2.0f
        imageSteps.animationDuration = 500


        previous.setOnClickListener { imageSteps.previousStep() }
        next.setOnClickListener { imageSteps.nextStep() }
    }

}