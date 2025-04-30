class Re:
    def getFileSuffixByPath(path):
        slen = len(path)-1
        while slen >= 0:
            if path[slen] == '.':
                break
            slen-=1
        return path[slen+1:]
    

if __name__ == '__main__':
    print(Re.getFileSuffixByPath("hello.tex"))