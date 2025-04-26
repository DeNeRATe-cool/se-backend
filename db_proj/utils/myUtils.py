import string
import random

class MyUtils:
    def get_code(place_num):
        characters = string.digits + string.ascii_uppercase
        res = ""
        for i in range(place_num):
            res += random.choice(characters)
        return res
    
    def getRandomBoolean():
        if random.randint(0,1) == 1:
            return True
        return False
    
    def random_by_probability(probability: float) -> bool:
        """
        根据给定概率生成 True 或 False。
        
        Args:
            probability (float): 返回 True 的概率，范围是 0.0 到 1.0。
            
        Returns:
            bool: 根据概率返回 True 或 False。
        """
        if not (0.0 <= probability <= 1.0):
            raise ValueError("probability 必须在 0.0 和 1.0 之间")
        
        return random.random() < probability