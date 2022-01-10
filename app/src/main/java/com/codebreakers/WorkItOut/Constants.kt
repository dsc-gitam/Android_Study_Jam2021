package com.codebreakers.WorkItOut

object Constants {
    fun defaultExcerciseList(): ArrayList<ExcerciseModel>{
      val excerciseList = ArrayList<ExcerciseModel>()
      val jumpingJacks = ExcerciseModel(1,"Jumping Jacks",R.drawable.jumping_jacks,false,false)
      excerciseList.add(jumpingJacks)

        val plank = ExcerciseModel(2,"Planks",R.drawable.plank,false,false)
        excerciseList.add(plank)

        val push_up_rotate = ExcerciseModel(3,"Rotational Planks",R.drawable.push_up_rotational,false,false)
        excerciseList.add(push_up_rotate)

        val pushUp = ExcerciseModel(4,"Push Up",R.drawable.push_up,false,false)
        excerciseList.add(pushUp)

        val side_plank = ExcerciseModel(5,"Side Plank",R.drawable.side_plank,false,false)
        excerciseList.add(side_plank)

        val standUpChair = ExcerciseModel(6,"Stand Up Chair",R.drawable.standup_chair,false,false)
        excerciseList.add(standUpChair)

        val squat = ExcerciseModel(7,"Squats",R.drawable.squat,false,false)
        excerciseList.add(squat)

        val triceps = ExcerciseModel(8,"Triceps",R.drawable.triceps,false,false)
        excerciseList.add(triceps)


      return excerciseList
    }
}