package com.epicdima.findwords.solver

import com.epicdima.findwords.trie.WordTrie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CoroutineSolver(linesSeparator: String, trie: WordTrie) : MultiThreadedSolver(linesSeparator, trie) {

    override fun ffff(matchedWords: List<WordAndMask>): Unit = runBlocking {
        val masks = getRawMasks(matchedWords)
        val minXY = getMinXAndMinY(masks)

        for (i in masks.indices) {
            if (masks[i].get(minXY[0], minXY[1])) {
                launch(Dispatchers.Default) {
                    val indexes = BooleanArray(masks.size)
                    indexes[i] = true
                    f2(masks[i].copy().or(originalMask), indexes, 0, masks)
                }
            }
        }
    }
}