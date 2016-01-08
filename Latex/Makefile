FILE := main
OUT  := build

pdf:
	# Also see .latexmkrc
	biber $(FILE)
	makeglossaries $(FILE)
	latexmk -outdir=$(OUT) -pdf $(FILE)

clean:
	rm -rf $(filter-out $(OUT)/$(FILE).pdf, $(wildcard $(OUT)/*))

purge:
	rm -rf $(OUT)

.PHONY: latexmk clean purge
