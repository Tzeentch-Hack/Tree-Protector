from transformers import pipeline

class TreeClassifier:
    def __init__(self, model_name=None):
        if model_name is None:
            self.model_name = "OttoYu/TreeClassification"
        else:
            self.model_name = model_name
        self.model = pipeline(task="image-classification", model=self.model_name)

    def classify(self, imp_path: str) -> list[dict]:
        model_answer = self.model(imp_path)
        return model_answer[:2]


if __name__ == '__main__':
    classifier = TreeClassifier()
    answer_1 = classifier.classify("../data/input/pine.jpg")
    answer_2 = classifier.classify("../data/input/tree_clf.jpg")
    answer_3 = classifier.classify("../data/input/bereza.jpg")
    print('answer1:', answer_1)
    print('answer2:', answer_2)
    print('answer3:', answer_3)
