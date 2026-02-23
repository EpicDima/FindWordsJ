package com.epicdima.findwords.solver

import com.epicdima.findwords.mask.MaskType
import com.epicdima.findwords.trie.WordTrie

class DeepRecursionSolver(
    linesSeparator: String,
    maskType: MaskType,
    wordTrie: WordTrie
) : DefaultSolver(linesSeparator, maskType, wordTrie) {

    override fun findFullMatches(matchedWords: List<WordAndMask>) {
        val matrix = createWordAndMaskMatrix(matchedWords)
        val mask = originalMask.copy()
        val result = mutableListOf<WordAndMask>()

        val deepRecursiveFunction = DeepRecursiveFunction { _ ->
            if (mask.isAllTrue()) {
                fullMatches.add(result.toList())
                return@DeepRecursiveFunction
            }

            var targetIndex = -1
            var minOptions = Int.MAX_VALUE

            for (i in 0 until rows * cols) {
                if (!mask[i]) {
                    var optionsCount = 0
                    for (wordAndMask in matrix[i / cols][i % cols]) {
                        if (mask.notIntersects(wordAndMask.mask)) {
                            optionsCount++
                        }
                    }

                    if (optionsCount < minOptions) {
                        minOptions = optionsCount
                        targetIndex = i
                        if (optionsCount <= 1) {
                            break
                        }
                    }
                }
            }

            if (targetIndex != -1) {
                for (positionWordAndMask in matrix[targetIndex / cols][targetIndex % cols]) {
                    if (mask.notIntersects(positionWordAndMask.mask)) {
                        result.add(positionWordAndMask)
                        mask.or(positionWordAndMask.mask)
                        callRecursive(targetIndex + 1)
                        result.removeLast()
                        mask.xor(positionWordAndMask.mask)
                    }
                }
            }
        }
        deepRecursiveFunction.invoke(0)
    }
}