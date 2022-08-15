package com.example.multiplechoice2

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.multiplechoice2.database.Question
import com.example.multiplechoice2.database.QuestionViewModel
import com.example.multiplechoice2.databinding.FragmentQuizBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [QuizFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QuizFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var isNext = false;
    private lateinit var binding: FragmentQuizBinding
    private val quizViewModel: QuestionViewModel by viewModels()
    private var score=0
    private lateinit var answerMap : Map<RadioButton, Int>
    private lateinit var buttonList : List<RadioButton>
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding=FragmentQuizBinding.inflate(layoutInflater)
        binding.buttonConfirm.setOnClickListener { buttonConfirmClickListener() }
        var question=quizViewModel.currentQuestion
        answerMap= mapOf(binding.radioButton1 to 1,binding.radioButton2 to 2,binding.radioButton3 to 3,binding.radioButton4 to 4)
        buttonList= listOf(binding.radioButton1,binding.radioButton2,binding.radioButton3,binding.radioButton4)
        setData(question)
        navController=findNavController()
        binding.imgBack.visibility=View.INVISIBLE
        binding.imgForward.visibility=View.INVISIBLE

        binding.imgBack.setOnClickListener {
            reviewBack()
        }

        binding.imgForward.setOnClickListener {
            reviewForward()
        }

        if(MainActivity.isQuizFinished){
            Log.d("onCreateView: ","isQuizFinished: "+quizViewModel.currentIndex)
            quizViewModel.setGoBackIndex()
            binding.buttonConfirm.visibility=View.INVISIBLE
            review()
        }
        Log.d("onCreateView: ","onCreateView: "+quizViewModel.currentIndex)

        val fm: FragmentManager = requireActivity().supportFragmentManager
        Log.i("Back stack count quiz: ", fm!!.backStackEntryCount.toString());
        for (entry in 0 until fm!!.backStackEntryCount) {
            Log.i("Back stack quiz: ","Found fragment: " + fm.getBackStackEntryAt(entry).id)
        }

        return binding.root
//        return inflater.inflate(R.layout.fragment_quiz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        loadFragment(this)

    }

    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun setData(question: Question){
        binding.txtQuizCount.text = "${quizViewModel.quizCount}/${quizViewModel.dataSize}"
        binding.txtQuestion.text=question.question
        binding.radioButton1.text=question.option_1
        binding.radioButton2.text=question.option_2
        binding.radioButton3.text=question.option_3
        binding.radioButton4.text=question.option_4
    }

    private fun buttonConfirmClickListener(){
        if(binding.buttonConfirm.text == "End"){
            navController.navigate(R.id.action_quizFragment_to_scoreFragment)
        }
        else if(!isNext) {
            confirm()
        }
        else {
            moveToNext()
        }

    }

    private fun confirm(){
        if(!isNext) {
            if(!isRadioButtonChecked()){
                Toast.makeText(this.context,"Please select an answer!", Toast.LENGTH_SHORT).show()
                return
            }
            checkQuestion()
            disableRadioButton()
            binding.txtAnswerExplanation.text="Explanation: "+ quizViewModel.currentQuestion.explanation
            if(quizViewModel.currentQuestion.uid==quizViewModel.dataSize){
                binding.buttonConfirm.text = "End"
                binding.imgBack.visibility=View.VISIBLE
                MainActivity.isQuizFinished=true
            }
            else {
                binding.buttonConfirm.text = "Next Question"
                isNext = true
            }
        }

    }

    private fun moveToNext(){
        if(isNext) {
            quizViewModel.moveToNext()
            quizViewModel.quizCount++
            binding.txtQuizCount.text = "$quizViewModel.quizCount++/${quizViewModel.dataSize}"
            var question = quizViewModel.currentQuestion
            uncheckRadioButton()
            setData(question)
            binding.buttonConfirm.text="Confirm"
            isNext = false
            enableRadioButton()
            binding.txtAnswerExplanation.text="Explanation: "
        }
    }

    private fun isRadioButtonChecked(): Boolean {
        return binding.radioButton1.isChecked || binding.radioButton2.isChecked || binding.radioButton3.isChecked || binding.radioButton4.isChecked
    }


    private fun disableRadioButton() {
        buttonList.forEach { button -> button.isEnabled = false }
    }

    private fun enableRadioButton() {
        buttonList.forEach { button -> button.isEnabled = true }
    }

    private fun uncheckRadioButton() {
        binding.radioGroup.clearCheck()
        buttonList.forEach { button -> button.setTextColor(Color.BLACK) }
    }

    private fun checkQuestion(){
        for ((button, answer) in answerMap){
            if(quizViewModel.currentQuestion.answer==answer){
                button.highlightColor= Color.BLUE
                button.setTextColor(Color.BLUE)
                if(button.isChecked) {
                    quizViewModel.currentQuestion.userAnswer=answer
                    score++
                }
                binding.txtScore.text="Scores: "+ score
            }
            else{
                button.setTextColor(Color.RED)
            }

        }
    }

    private fun reviewBack(){
        if(quizViewModel.quizCount>0){
            quizViewModel.quizCount--
        }

        if(quizViewModel.quizCount==1){
            binding.imgBack.visibility=View.INVISIBLE
        }

        if(binding.imgForward.visibility==View.INVISIBLE){
            binding.imgForward.visibility=View.VISIBLE
        }

        quizViewModel.goBack()
        Log.d("quizViewModel", quizViewModel.currentIndex.toString())
        setData(quizViewModel.currentQuestion)
        binding.txtQuizCount.text = "${quizViewModel.quizCount}/${quizViewModel.dataSize}"
        for ((button, answer) in answerMap) {
            if (quizViewModel.currentQuestion.userAnswer == answer) {
                button.isChecked = true
            }
            if (quizViewModel.currentQuestion.answer == answer) {
                button.highlightColor = Color.BLUE
                button.setTextColor(Color.BLUE)
            }
            else{
                button.setTextColor(Color.RED)
            }
        }

    }



    private fun review(){
        disableRadioButton()
        if(quizViewModel.quizCount>=quizViewModel.dataSize-1){
            binding.imgForward.visibility=View.INVISIBLE
        }

        if(binding.imgBack.visibility==View.INVISIBLE){
            binding.imgBack.visibility=View.VISIBLE
        }
        setData(quizViewModel.currentQuestion)
        binding.txtQuizCount.text = "${quizViewModel.quizCount}/${quizViewModel.dataSize}"
        reviewQuestion()

    }

    private fun reviewForward(){
        if(quizViewModel.quizCount<quizViewModel.dataSize){
            quizViewModel.quizCount++
        }

        if(quizViewModel.quizCount==quizViewModel.dataSize){
            binding.imgForward.visibility=View.INVISIBLE
        }

        if(binding.imgBack.visibility==View.INVISIBLE){
            binding.imgBack.visibility=View.VISIBLE
        }
        quizViewModel.moveToNext()
        Log.d("quizViewModel", quizViewModel.currentIndex.toString())
        setData(quizViewModel.currentQuestion)
        binding.txtQuizCount.text = "${quizViewModel.quizCount}/${quizViewModel.dataSize}"
        reviewQuestion()
    }

    private fun reviewQuestion(){
        for ((button, answer) in answerMap) {
            if (quizViewModel.currentQuestion.userAnswer == answer) {
                button.isChecked = true
            }
            if (quizViewModel.currentQuestion.answer == answer) {
                button.highlightColor = Color.BLUE
                button.setTextColor(Color.BLUE)
            }
            else{
                button.setTextColor(Color.RED)
            }
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment QuizFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            QuizFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}