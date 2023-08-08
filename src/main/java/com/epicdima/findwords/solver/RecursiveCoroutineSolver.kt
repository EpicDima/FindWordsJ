package com.epicdima.findwords.solver

import com.epicdima.findwords.mask.Mask
import com.epicdima.findwords.mask.MaskType
import com.epicdima.findwords.trie.WordTrie

class RecursiveCoroutineSolver(
    linesSeparator: String,
    maskType: MaskType,
    wordTrie: WordTrie
) : DefaultSolver(linesSeparator, maskType, wordTrie) {

    override fun ffff(matchedWords: List<WordAndMask>) {
        val matrix = createWordAndMaskMatrix(matchedWords)

        val deepRecursiveFunction = DeepRecursiveFunction<F2Params, Unit> {
            if (it.mask.isAllTrue()) {
                fullMatches.add(it.result)
                return@DeepRecursiveFunction
            }
            for (i in it.startIndex until rows * cols) {
                if (it.mask[i]) {
                    continue
                }
                for (positionWordAndMask in matrix[i / cols][i % cols]) {
                    if (it.mask.notIntersects(positionWordAndMask.mask)) {
                        val tempResult = mutableListOf<WordAndMask>()
                        tempResult.addAll(it.result)
                        tempResult.add(positionWordAndMask)
                        callRecursive(F2Params(it.mask.copy().or(positionWordAndMask.mask), i + 1, tempResult))
                    }
                }
                break
            }
        }
        deepRecursiveFunction.invoke(F2Params(originalMask.copy(), 0, ArrayList()))
    }

    private class F2Params(
        val mask: Mask,
        val startIndex: Int,
        val result: List<WordAndMask>
    )
}