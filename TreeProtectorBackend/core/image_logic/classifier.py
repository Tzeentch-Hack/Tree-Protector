from ml.tree_classifier import TreeClassifier
from core.schemas import schemas

classifier = TreeClassifier()


def get_tree_kind(image_path: str):
    result = classifier.classify(image_path)
    first_tree_type = schemas.TreeType(score=result[0]["score"]*100, label=result[0]["label"])
    second_tree_type = schemas.TreeType(score=result[1]["score"]*100, label=result[1]["label"])
    schema_result = schemas.TreeClassifierResult(first_tree_type=first_tree_type, second_tree_type=second_tree_type)
    return schema_result
