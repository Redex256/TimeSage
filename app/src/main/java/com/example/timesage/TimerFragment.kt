package com.example.timesage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.os.CountDownTimer
import androidx.core.view.isVisible

import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.timesage.databinding.FragmentTimerBinding


class TimerFragment : Fragment() {

    private lateinit var binding: FragmentTimerBinding
    private lateinit var navController: NavController
    private var timer: CountDownTimer? = null
    var timeleft: Long = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimerBinding.inflate(inflater, container, false)
        return binding.root
    }
    private fun init(view: View) {
        navController = Navigation.findNavController(view)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        binding.btnresume.visibility = View.GONE
        binding.btnstop.visibility = View.GONE
        binding.Main.setOnClickListener {
            navController.navigate(R.id.action_timerFragment_to_homeFragment)
        }
        binding.btnstart.setOnClickListener {
            val time = binding.timePomodoro
                .text.toString().toLong()
            val timemin = time*60*1000
            countDownTimer(timemin)
            binding.btnPause.visibility = View.VISIBLE
            binding.btnstop.visibility = View.VISIBLE
            binding.btnstart.visibility = View.GONE
        }
        binding.btnPause.setOnClickListener {
            onPause()
            binding.btnresume.visibility = View.VISIBLE
            binding.btnPause.visibility = View.GONE
        }
        binding.btnresume.setOnClickListener {
            countDownTimer(timeleft)
            binding.btnPause.visibility = View.VISIBLE
            binding.btnresume.visibility = View.GONE
        }
        binding.btnstop.setOnClickListener {
            onStop()
            binding.btnstart.visibility = View.VISIBLE
            binding.btnstop.visibility = View.GONE
            binding.btnPause.visibility = View.GONE
        }
    }


    private fun countDownTimer(time: Long){
        timer?.cancel()
        timer = object : CountDownTimer(time,1000){
                  override fun onTick(millisUntilFinished: Long) {
                      val hour = (millisUntilFinished/1000) /3600
                      val sec = (millisUntilFinished/1000)% 60
                      val min = ((millisUntilFinished/1000) % 3600) /60
                      timeleft = millisUntilFinished
                      val timerpomodoro = "%02d:%02d:%02d".format(hour,min,sec)
                          binding.pomodoroTimer.text = timerpomodoro
                  }
                  override fun onFinish() {
                      binding.pomodoroTimer.text = "Завершено"
                      binding.btnstart.visibility = View.VISIBLE
                      binding.btnstop.visibility = View.GONE
                      binding.btnPause.visibility = View.GONE
                  }
        }.start()
    }
    override fun onPause() {
        super.onPause()
        timer?.cancel()
    }

    override fun onStop() {
        super.onStop()
        timer?.cancel()
        binding.pomodoroTimer.text = "00:00:00"
    }
}
