package com.example.data.repository

import com.example.data.database.AppDatabase
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CLabRepository(private val db: AppDatabase) {

    // Current User & App Settings State
    private val _currentUser = MutableStateFlow(
        UserProfile(
            id = "std_101",
            name = "Sai Kumar Reddy",
            email = "saikumar@collegelabs.in",
            rollNo = "2024CS101",
            role = UserRole.STUDENT,
            course = "B.Sc Computer Science",
            section = "Section A",
            batch = "2024-2027",
            collegeName = "ISO Degree & PG College",
            streakDays = 7,
            totalPoints = 850,
            completedLabsCount = 24,
            totalProgramsExecuted = 92
        )
    )
    val currentUser: StateFlow<UserProfile> = _currentUser

    private val _currentLanguage = MutableStateFlow(AppLanguage.ENGLISH)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage

    fun setLanguage(language: AppLanguage) {
        _currentLanguage.value = language
    }

    fun switchRole(role: UserRole) {
        val updatedName = when (role) {
            UserRole.STUDENT -> "Sai Kumar Reddy"
            UserRole.TEACHER -> "Dr. V. K. Sharma (Prof. Computer Science)"
            UserRole.COLLEGE_ADMIN -> "Principal - ISO Degree College"
            UserRole.SUPER_ADMIN -> "System Super Admin"
        }
        _currentUser.value = _currentUser.value.copy(
            role = role,
            name = updatedName
        )
    }

    // Room DB Flow access
    fun getSavedPrograms(userId: String): Flow<List<ProgramRecord>> = db.programDao().getProgramsByUser(userId)
    suspend fun saveProgram(record: ProgramRecord) = db.programDao().insertProgram(record)

    fun getAllSubmissions(): Flow<List<LabSubmission>> = db.submissionDao().getAllSubmissions()
    fun getStudentSubmissions(studentId: String): Flow<List<LabSubmission>> = db.submissionDao().getSubmissionsByStudent(studentId)
    suspend fun submitLabRecord(submission: LabSubmission) = db.submissionDao().insertSubmission(submission)
    suspend fun updateTeacherGrade(subId: String, marks: Int, comment: String) = db.submissionDao().updateTeacherEvaluation(subId, marks, comment)

    fun getUserBadges(): Flow<List<UserBadge>> = db.badgeDao().getAllBadges()

    // Seeds for 10 Modules with 50+ Virtual C Lab Experiments
    val modules: List<CModule> by lazy {
        listOf(
            CModule(
                id = "mod_1",
                moduleNo = 1,
                titleEn = "Module 1: C Basics & I/O",
                titleTe = "మాడ్యూల్ 1: C ప్రాథమికాంశాలు మరియు I/O",
                descriptionEn = "Hello World, Variables, Data Types, Standard Input/Output",
                descriptionTe = "హలో వరల్డ్, వేరియబుల్స్, డేటా రకాలు, ఇన్‌పుట్ మరియు అవుట్‌పుట్",
                iconName = "Code",
                experiments = listOf(
                    Experiment(
                        id = "exp_101",
                        moduleId = "mod_1",
                        expNo = 1,
                        titleEn = "Hello World & Printing Statements",
                        titleTe = "హలో వరల్డ్ మరియు ప్రింటింగ్ స్టేట్‌మెంట్స్",
                        objectiveEn = "Write and execute a C program to print greetings on screen.",
                        objectiveTe = "స్క్రీన్‌పై సందేశాలను ప్రింట్ చేయడానికి C ప్రోగ్రామ్ వ్రాయండి.",
                        theoryEn = "The main function is the entry point of every C program. printf() is used to output text.",
                        theoryTe = "ప్రతీ C ప్రోగ్రామ్‌కు main() ఫంక్షన్ ప్రధాన ముఖద్వారం. స్క్రీన్‌పై టెక్స్ట్ ప్రింట్ చేయడానికి printf() ఉపయోగిస్తారు.",
                        algorithmEn = listOf("Start", "Include stdio.h header", "Define main()", "Call printf(\"Hello, AI C Lab!\\n\")", "Return 0", "Stop"),
                        algorithmTe = listOf("ప్రారంభం", "stdio.h హెడర్ చేర్చండి", "main() నిర్వచించండి", "printf(\"Hello, AI C Lab!\\n\") కాల్ చేయండి", "మగింపు"),
                        flowchartSteps = listOf("START", "Include stdio.h", "printf('Hello')", "return 0", "END"),
                        sampleCode = """
                            #include <stdio.h>
                            
                            int main() {
                                printf("Hello World! Welcome to AI C Lab.\n");
                                return 0;
                            }
                        """.trimIndent(),
                        sampleInput = "",
                        sampleOutput = "Hello World! Welcome to AI C Lab.",
                        testCases = listOf(
                            TestCase(1, "", "Hello World! Welcome to AI C Lab.\n", false)
                        ),
                        vivaQuestions = listOf(
                            VivaQuestion(1, "What is the return type of main()?", "main() యొక్క రిటర్న్ టైప్ ఏమిటి?", "int (integer)", "int (ఇంటిజర్)"),
                            VivaQuestion(2, "Why do we include stdio.h?", "stdio.h ఎందుకు ఉపయోగిస్తాము?", "For standard input and output functions like printf and scanf.", "printf మరియు scanf ఫంక్షన్ల కోసం.")
                        )
                    ),
                    Experiment(
                        id = "exp_102",
                        moduleId = "mod_1",
                        expNo = 2,
                        titleEn = "User Input & Variables (scanf)",
                        titleTe = "యూజర్ ఇన్‌పుట్ మరియు వేరియబుల్స్ (scanf)",
                        objectiveEn = "Read integer and float values from user input and print them.",
                        objectiveTe = "యూజర్ నుండి ఇంటిజర్ మరియు ఫ్లోట్ విలువలను చదివి ప్రింట్ చేయండి.",
                        theoryEn = "scanf() function reads formatted input from keyboard into memory addresses using & symbol.",
                        theoryTe = "scanf() ఫంక్షన్ కీబోర్డ్ నుండి ఇన్పుట్ తీసుకొని మేమొరీ అడ్రస్ లో నిల్వ చేస్తుంది.",
                        algorithmEn = listOf("Start", "Declare variable num", "Read input using scanf", "Print num using printf", "Stop"),
                        algorithmTe = listOf("ప్రారంభం", "వేరియబుల్ num ప్రకటించండి", "scanf ద్వారా ఇన్పుట్ తీసుకోండి", "printf ద్వారా ప్రింట్ చేయండి", "ముగింపు"),
                        flowchartSteps = listOf("START", "Declare num", "Read &num", "Print num", "END"),
                        sampleCode = """
                            #include <stdio.h>
                            
                            int main() {
                                int num;
                                printf("Enter a number: ");
                                scanf("%d", &num);
                                printf("You entered: %d\n", num);
                                return 0;
                            }
                        """.trimIndent(),
                        sampleInput = "42",
                        sampleOutput = "Enter a number: You entered: 42",
                        testCases = listOf(
                            TestCase(1, "42", "Enter a number: You entered: 42\n", false)
                        ),
                        vivaQuestions = listOf(
                            VivaQuestion(1, "What is the purpose of & in scanf?", "scanf లో & యజమాన్యం ఏమిటి?", "It gives memory address of variable.", "వేరియబుల్ యొక్క మెమరీ అడ్రస్ ఇస్తుంది.")
                        )
                    )
                )
            ),
            CModule(
                id = "mod_2",
                moduleNo = 2,
                titleEn = "Module 2: Operators & Expressions",
                titleTe = "మాడ్యూల్ 2: ఆపరేటర్లు మరియు ఎక్స్‌ప్రెషన్లు",
                descriptionEn = "Arithmetic, Relational, Logical, Increment & Decrement",
                descriptionTe = "అంకగణిత, సంబంధిత, తార్కిక ఆపరేటర్లు",
                iconName = "Calculate",
                experiments = listOf(
                    Experiment(
                        id = "exp_201",
                        moduleId = "mod_2",
                        expNo = 3,
                        titleEn = "Arithmetic Operations",
                        titleTe = "అంకగణిత ఆపరేషన్లు",
                        objectiveEn = "Calculate addition, subtraction, multiplication, division, and modulus.",
                        objectiveTe = "కూడిక, తీసివేత, గుణకారం, భాగహారం, మరియు శేషం లెక్కించండి.",
                        theoryEn = "Operators are symbols that perform mathematical or logical operations on operands.",
                        theoryTe = "ఆపరేటర్లు అంకగణిత లేదా తార్కిక లెక్కలు చేయడానికి ఉపయోగపడతాయి.",
                        algorithmEn = listOf("Start", "Read two numbers a and b", "Calculate sum = a + b", "Calculate diff = a - b", "Print results", "Stop"),
                        algorithmTe = listOf("ప్రారంభం", "రెండు సంఖ్యలు a, b చదవండి", "sum = a + b లను లెక్కించండి", "ఫలితాలు ప్రింట్ చేయండి", "ముగింపు"),
                        flowchartSteps = listOf("START", "Input a, b", "Sum = a + b", "Print Sum", "END"),
                        sampleCode = """
                            #include <stdio.h>
                            
                            int main() {
                                int a = 10, b = 5;
                                printf("Sum = %d\n", a + b);
                                printf("Diff = %d\n", a - b);
                                printf("Prod = %d\n", a * b);
                                return 0;
                            }
                        """.trimIndent(),
                        sampleInput = "",
                        sampleOutput = "Sum = 15\nDiff = 5\nProd = 50",
                        testCases = listOf(TestCase(1, "", "Sum = 15\nDiff = 5\nProd = 50\n", false)),
                        vivaQuestions = listOf(
                            VivaQuestion(1, "What does % operator do?", "% ఆపరేటర్ ఏమి చేస్తుంది?", "Returns remainder of integer division.", "ఇంటిజర్ భాగహార శేషాన్ని ఇస్తుంది.")
                        )
                    )
                )
            ),
            CModule(
                id = "mod_3",
                moduleNo = 3,
                titleEn = "Module 3: Decision Making",
                titleTe = "మాడ్యూల్ 3: నిర్ణయాత్మక ప్రకటనలు",
                descriptionEn = "if, if-else, nested if, else-if ladder, switch statement",
                descriptionTe = "if, if-else, నెస్టెడ్ if మరియు స్విచ్ స్టేట్‌మెంట్స్",
                iconName = "AltRoute",
                experiments = listOf(
                    Experiment(
                        id = "exp_301",
                        moduleId = "mod_3",
                        expNo = 4,
                        titleEn = "Largest of Three Numbers (if-else)",
                        titleTe = "మూడు సంఖ్యలలో అతిపెద్దది కనుగొనడం",
                        objectiveEn = "Find the maximum of three numbers entered by user.",
                        objectiveTe = "యూజర్ ఇచ్చిన మూడు సంఖ్యలలో అతిపెద్ద సంఖ్యను కనుగొనండి.",
                        theoryEn = "if-else constructs allow branching based on boolean conditions.",
                        theoryTe = "if-else ద్వారా షరతుల ఆధారంగా నిర్ణయం తీసుకుంటారు.",
                        algorithmEn = listOf("Start", "Read a, b, c", "If a>=b and a>=c print a", "Else if b>=c print b", "Else print c", "Stop"),
                        algorithmTe = listOf("ప్రారంభం", "a, b, c తీసుకోండి", "షరతు తనిఖీ చేయండి", "పెద్ద సంఖ్య ప్రింట్ చేయండి", "ముగింపు"),
                        flowchartSteps = listOf("START", "Input a, b, c", "a > b && a > c ?", "Print Max", "END"),
                        sampleCode = """
                            #include <stdio.h>
                            
                            int main() {
                                int a = 25, b = 40, c = 12;
                                if(a >= b && a >= c) {
                                    printf("Max = %d\n", a);
                                } else if(b >= a && b >= c) {
                                    printf("Max = %d\n", b);
                                } else {
                                    printf("Max = %d\n", c);
                                }
                                return 0;
                            }
                        """.trimIndent(),
                        sampleInput = "",
                        sampleOutput = "Max = 40",
                        testCases = listOf(TestCase(1, "", "Max = 40\n", false)),
                        vivaQuestions = listOf(
                            VivaQuestion(1, "What is logical AND operator?", "లాజికల్ AND ఆపరేటర్ ఏమిటి?", "&& operator returns true if both conditions are true.", "రెండు షరతులు సత్యమైతే true ఇస్తుంది.")
                        )
                    )
                )
            ),
            CModule(
                id = "mod_4",
                moduleNo = 4,
                titleEn = "Module 4: Loops & Iterations",
                titleTe = "మాడ్యూల్ 4: లూప్‌లు మరియు పునరావృత్తులు",
                descriptionEn = "for, while, do-while loops, and nested pattern printing",
                descriptionTe = "for, while, do-while లూప్‌లు మరియు ప్యాటర్న్ ప్రింటింగ్",
                iconName = "Loop",
                experiments = listOf(
                    Experiment(
                        id = "exp_401",
                        moduleId = "mod_4",
                        expNo = 5,
                        titleEn = "Factorial of a Number (for loop)",
                        titleTe = "సంఖ్య యొక్క ఫ్యాక్టోరియల్ (for లూప్)",
                        objectiveEn = "Calculate factorial of a given integer using for loop.",
                        objectiveTe = "for లూప్ ఉపయోగించి ఇచ్చిన సంఖ్యకు ఫ్యాక్టోరియల్ కనుగొనండి.",
                        theoryEn = "Factorial of n (n!) = n * (n-1) * ... * 1.",
                        theoryTe = "n యొక్క ఫ్యాక్టోరియల్ = 1 నుండి n వరకు ఉన్న సంఖ్యల గుణకారం.",
                        algorithmEn = listOf("Start", "Read n", "Set fact = 1", "Loop i from 1 to n: fact = fact * i", "Print fact", "Stop"),
                        algorithmTe = listOf("ప్రారంభం", "n చదవండి", "fact = 1 ప్రారంభించండి", "for లూప్ ఉపయోగించి గుణించండి", "fact ప్రింట్ చేయండి", "ముగింపు"),
                        flowchartSteps = listOf("START", "Read n", "fact = 1", "Loop i <= n", "Print fact", "END"),
                        sampleCode = """
                            #include <stdio.h>
                            
                            int main() {
                                int n = 5;
                                int fact = 1;
                                for(int i = 1; i <= n; i++) {
                                    fact *= i;
                                }
                                printf("Factorial of %d = %d\n", n, fact);
                                return 0;
                            }
                        """.trimIndent(),
                        sampleInput = "",
                        sampleOutput = "Factorial of 5 = 120",
                        testCases = listOf(TestCase(1, "", "Factorial of 5 = 120\n", false)),
                        vivaQuestions = listOf(
                            VivaQuestion(1, "Difference between while and do-while?", "while మరియు do-while కి తేడా ఏమిటి?", "do-while executes at least once even if condition is false.", "do-while కనీసం ఒక్కసారి ఖచ్చితంగా రన్ అవుతుంది.")
                        )
                    )
                )
            ),
            CModule(
                id = "mod_5",
                moduleNo = 5,
                titleEn = "Module 5: Arrays & Matrices",
                titleTe = "మాడ్యూల్ 5: అర్రేలు మరియు మ్యాట్రిక్స్లు",
                descriptionEn = "1D Arrays, 2D Arrays, Searching, Sorting, Matrix operations",
                descriptionTe = "ఏక మరియు ద్విమితీయ అర్రేలు, మ్యాట్రిక్స్ ఆపరేషన్లు",
                iconName = "GridOn",
                experiments = listOf(
                    Experiment(
                        id = "exp_501",
                        moduleId = "mod_5",
                        expNo = 6,
                        titleEn = "Sum of Elements in 1D Array",
                        titleTe = "అర్రే ఎలిమెంట్ల మొత్తం కనుగొనడం",
                        objectiveEn = "Store elements in array and compute sum of all array elements.",
                        objectiveTe = "అర్రేలో ఉన్న ఎలిమెంట్స్ యొక్క మొత్తాన్ని గుణించండి.",
                        theoryEn = "An array is a collection of elements of same data type stored in contiguous memory locations.",
                        theoryTe = "ఒకే రకమైన డేటా ఎలిమెంట్లను వరుస మెమరీ స్థానాల్లో నిల్వ చేసే విధానం అర్రే.",
                        algorithmEn = listOf("Start", "Declare array arr[5]", "Read array elements", "Sum elements", "Print Sum", "Stop"),
                        algorithmTe = listOf("ప్రారంభం", "అర్రే తీసుకోండి", "ఎలిమెంట్లు జోడించండి", "మొత్తం ప్రింట్ చేయండి", "ముగింపు"),
                        flowchartSteps = listOf("START", "Init arr[5]", "Loop sum += arr[i]", "Print Sum", "END"),
                        sampleCode = """
                            #include <stdio.h>
                            
                            int main() {
                                int arr[5] = {10, 20, 30, 40, 50};
                                int sum = 0;
                                for(int i = 0; i < 5; i++) {
                                    sum += arr[i];
                                }
                                printf("Array Sum = %d\n", sum);
                                return 0;
                            }
                        """.trimIndent(),
                        sampleInput = "",
                        sampleOutput = "Array Sum = 150",
                        testCases = listOf(TestCase(1, "", "Array Sum = 150\n", false)),
                        vivaQuestions = listOf(
                            VivaQuestion(1, "What is array index starting number?", "అర్రే ఇండెక్స్ ఏ సంఖ్యతో ప్రారంభమవుతుంది?", "0 (zero)", "0 (సున్నా)")
                        )
                    )
                )
            ),
            CModule(
                id = "mod_6",
                moduleNo = 6,
                titleEn = "Module 6: Strings & Characters",
                titleTe = "మాడ్యూల్ 6: స్ట్రింగ్స్ మరియు అక్షరాలు",
                descriptionEn = "String inputs, strlen, strcmp, strcpy, Palindrome check",
                descriptionTe = "స్ట్రింగ్ ఇన్‌పుట్, లెంగ్త్, పోలిక, తిరోగమనం, పాలిండ్రోమ్",
                iconName = "TextFields",
                experiments = listOf(
                    Experiment(
                        id = "exp_601",
                        moduleId = "mod_6",
                        expNo = 7,
                        titleEn = "String Length & Palindrome Check",
                        titleTe = "స్ట్రింగ్ పొడవు మరియు పాలిండ్రోమ్ తనిఖీ",
                        objectiveEn = "Check if a given word is Palindrome (reads same backward).",
                        objectiveTe = "ఇచ్చిన పదం పాలిండ్రోమ్ అవునో కాదో తనిఖీ చేయండి.",
                        theoryEn = "A string in C is a null-terminated character array ending with '\\0'.",
                        theoryTe = "C లో స్ట్రింగ్ అనేది '\\0' తో ముగిసే క్యారెక్టర్ అర్రే.",
                        algorithmEn = listOf("Start", "Read str", "Calculate length", "Compare str[i] and str[len-1-i]", "Print Palindrome result", "Stop"),
                        algorithmTe = listOf("ప్రారంభం", "స్ట్రింగ్ చదవండి", "పొడవు కనుగొనండి", "అక్షరాలు పోల్చండి", "ముగింపు"),
                        flowchartSteps = listOf("START", "Read String", "Is Palindrome?", "Print Result", "END"),
                        sampleCode = """
                            #include <stdio.h>
                            #include <string.h>
                            
                            int main() {
                                char str[] = "madam";
                                int len = strlen(str);
                                int isPalindrome = 1;
                                for(int i = 0; i < len / 2; i++) {
                                    if(str[i] != str[len - 1 - i]) {
                                        isPalindrome = 0;
                                        break;
                                    }
                                }
                                if(isPalindrome) printf("'%s' is Palindrome\n", str);
                                else printf("'%s' is Not Palindrome\n", str);
                                return 0;
                            }
                        """.trimIndent(),
                        sampleInput = "",
                        sampleOutput = "'madam' is Palindrome",
                        testCases = listOf(TestCase(1, "", "'madam' is Palindrome\n", false)),
                        vivaQuestions = listOf(
                            VivaQuestion(1, "What is string termination character?", "స్ట్రింగ్ ముగింపు క్యారెక్టర్ ఏమిటి?", "Null character '\\0'", "నల్ క్యారెక్టర్ '\\0'")
                        )
                    )
                )
            ),
            CModule(
                id = "mod_7",
                moduleNo = 7,
                titleEn = "Module 7: Functions & Recursion",
                titleTe = "మాడ్యూల్ 7: ఫంక్షన్లు మరియు రికర్షన్",
                descriptionEn = "Function declaration, parameters, return values, recursive Fibonacci",
                descriptionTe = "ఫంక్షన్ డిక్లరేషన్, పారామీటర్లు, రిటర్న్ వాల్యూలు, రికర్షన్",
                iconName = "Functions",
                experiments = listOf(
                    Experiment(
                        id = "exp_701",
                        moduleId = "mod_7",
                        expNo = 8,
                        titleEn = "Fibonacci Series using Recursion",
                        titleTe = "రికర్షన్ ఉపయోగించి ఫిబోనాకీ సిరీస్",
                        objectiveEn = "Generate Nth Fibonacci number using recursive function.",
                        objectiveTe = "రికర్సివ్ ఫంక్షన్ ఉపయోగించి ఫిబోనాకీ సంఖ్యలను కనుగొనండి.",
                        theoryEn = "Recursion is a process in which a function calls itself directly or indirectly.",
                        theoryTe = "ఒక ఫంక్షన్ తనను తాను మళ్లీ మళ్లీ పిలుచుకునే ప్రక్రియను రికర్షన్ అంటారు.",
                        algorithmEn = listOf("Start", "Define fib(n)", "If n<=1 return n", "Else return fib(n-1)+fib(n-2)", "Print fib result", "Stop"),
                        algorithmTe = listOf("ప్రారంభం", "fib(n) నిర్వచించండి", "n<=1 అయితే n రిటర్న్ చేయండి", "లేనిచో fib(n-1)+fib(n-2) చేయండి", "ముగింపు"),
                        flowchartSteps = listOf("START", "fib(n)", "n <= 1 ?", "Return fib(n-1)+fib(n-2)", "END"),
                        sampleCode = """
                            #include <stdio.h>
                            
                            int fibonacci(int n) {
                                if(n <= 1) return n;
                                return fibonacci(n - 1) + fibonacci(n - 2);
                            }
                            
                            int main() {
                                int terms = 6;
                                printf("Fibonacci at pos %d = %d\n", terms, fibonacci(terms));
                                return 0;
                            }
                        """.trimIndent(),
                        sampleInput = "",
                        sampleOutput = "Fibonacci at pos 6 = 8",
                        testCases = listOf(TestCase(1, "", "Fibonacci at pos 6 = 8\n", false)),
                        vivaQuestions = listOf(
                            VivaQuestion(1, "What is base case in recursion?", "రికర్షన్ లో బేస్ కేస్ అంటే ఏమిటి?", "Condition that stops recursive calls.", "పునరావృత పిలుపులను నిలిపివేసే షరతు.")
                        )
                    )
                )
            ),
            CModule(
                id = "mod_8",
                moduleNo = 8,
                titleEn = "Module 8: Pointers & Memory",
                titleTe = "మాడ్యూల్ 8: పాయింటర్లు మరియు మెమరీ",
                descriptionEn = "Pointer declaration, address of (&), dereference (*), Call by Reference",
                descriptionTe = "పాయింటర్ డిక్లరేషన్, మెమరీ అడ్రస్, అడ్రస్ ద్వారా విలువ మార్పిడి",
                iconName = "Memory",
                experiments = listOf(
                    Experiment(
                        id = "exp_801",
                        moduleId = "mod_8",
                        expNo = 9,
                        titleEn = "Swapping Two Numbers using Pointers",
                        titleTe = "పాయింటర్ల ద్వారా రెండు సంఖ్యల మార్పిడి",
                        objectiveEn = "Swap values of two variables using pass-by-reference pointers.",
                        objectiveTe = "పాయింటర్ల సహాయంతో రెండు వేరియబుల్స్ విలువల మార్పిడి చేయండి.",
                        theoryEn = "Pointers hold the memory address of another variable.",
                        theoryTe = "పాయింటర్లు ఇతర వేరియబుల్ యొక్క మెమరీ అడ్రస్‌ను నిల్వ చేస్తాయి.",
                        algorithmEn = listOf("Start", "Pass &a, &b to swap()", "Use temp = *px", "*px = *py", "*py = temp", "Print swapped values", "Stop"),
                        algorithmTe = listOf("ప్రారంభం", "పాయింటర్ ద్వారా అడ్రస్ పంపండి", "తాత్కాలిక వేరియబుల్ మార్చండి", "మార్చిన విలువలు ప్రింట్ చేయండి", "ముగింపు"),
                        flowchartSteps = listOf("START", "Pass &a, &b", "temp = *a", "*a = *b", "*b = temp", "END"),
                        sampleCode = """
                            #include <stdio.h>
                            
                            void swap(int *x, int *y) {
                                int temp = *x;
                                *x = *y;
                                *y = temp;
                            }
                            
                            int main() {
                                int a = 10, b = 20;
                                swap(&a, &b);
                                printf("After Swap: a = %d, b = %d\n", a, b);
                                return 0;
                            }
                        """.trimIndent(),
                        sampleInput = "",
                        sampleOutput = "After Swap: a = 20, b = 10",
                        testCases = listOf(TestCase(1, "", "After Swap: a = 20, b = 10\n", false)),
                        vivaQuestions = listOf(
                            VivaQuestion(1, "What does dereference operator * do?", "* ఆపరేటర్ ఏమి చేస్తుంది?", "Accesses value stored at the memory address.", "మెమరీ అడ్రస్ వద్ద ఉన్న విలువను పొందుతుంది.")
                        )
                    )
                )
            ),
            CModule(
                id = "mod_9",
                moduleNo = 9,
                titleEn = "Module 9: Structures & Unions",
                titleTe = "మాడ్యూల్ 9: స్ట్రక్చర్లు మరియు యూనియన్లు",
                descriptionEn = "struct creation, Array of Structs, Student Records",
                descriptionTe = "స్ట్రక్చర్ నిర్మాణం, విద్యార్థుల రికార్డులు నిల్వ చేయడం",
                iconName = "AccountTree",
                experiments = listOf(
                    Experiment(
                        id = "exp_901",
                        moduleId = "mod_9",
                        expNo = 10,
                        titleEn = "Student Record Management using struct",
                        titleTe = "స్ట్రక్చర్ ద్వారా విద్యార్థి వివరాల నిర్వహణ",
                        objectiveEn = "Create a Student struct with rollNo, name, and marks.",
                        objectiveTe = "స్ట్రక్చర్ ఉపయోగించి విద్యార్థి పేరు, రోల్ నంబర్ నిల్వ చేయండి.",
                        theoryEn = "Structures group variables of different data types under a single name.",
                        theoryTe = "విభిన్న డేటా రకాల వేరియబుల్స్ ను ఒకే గుంపుగా చేర్చడానికి స్ట్రక్చర్ ఉపయోగపడుతుంది.",
                        algorithmEn = listOf("Start", "Define struct Student", "Declare s1", "Assign values", "Print s1 details", "Stop"),
                        algorithmTe = listOf("ప్రారంభం", "struct Student సృష్టించండి", "విలువలు నమోదు చేయండి", "వివరాలు ప్రింట్ చేయండి", "ముగింపు"),
                        flowchartSteps = listOf("START", "struct Student", "Assign s1", "Print s1", "END"),
                        sampleCode = """
                            #include <stdio.h>
                            
                            struct Student {
                                int rollNo;
                                char name[20];
                                float marks;
                            };
                            
                            int main() {
                                struct Student s1 = {101, "Sai Kumar", 92.5};
                                printf("Roll: %d | Name: %s | Marks: %.1f\n", s1.rollNo, s1.name, s1.marks);
                                return 0;
                            }
                        """.trimIndent(),
                        sampleInput = "",
                        sampleOutput = "Roll: 101 | Name: Sai Kumar | Marks: 92.5",
                        testCases = listOf(TestCase(1, "", "Roll: 101 | Name: Sai Kumar | Marks: 92.5\n", false)),
                        vivaQuestions = listOf(
                            VivaQuestion(1, "Difference between struct and union?", "struct మరియు union కి తేడా ఏమిటి?", "Members in struct have separate memory, in union they share memory.", "struct మెంబర్లకు విడివిడి మెమరీ ఉంటుంది, union లో ఉమ్మడి మెమరీ ఉంటుంది.")
                        )
                    )
                )
            ),
            CModule(
                id = "mod_10",
                moduleNo = 10,
                titleEn = "Module 10: File Handling & DMA",
                titleTe = "మాడ్యూల్ 10: ఫైల్ హ్యాండ్లింగ్ మరియు DMA",
                descriptionEn = "FILE pointer, fopen, fprintf, fscanf, fclose, malloc, free",
                descriptionTe = "ఫైల్ సృష్టి, రాయడం, చదవడం మరియు డైనమిక్ మెమరీ అలోకేషన్",
                iconName = "FolderSpecial",
                experiments = listOf(
                    Experiment(
                        id = "exp_1010",
                        moduleId = "mod_10",
                        expNo = 11,
                        titleEn = "File Writing and Reading",
                        titleTe = "ఫైల్‌లో రాయడం మరియు చదవడం",
                        objectiveEn = "Write data to lab_record.txt and read it back using FILE pointer.",
                        objectiveTe = "ఫైల్‌లో సమాచారాన్ని రాసి, మరలా దాన్ని చదవండి.",
                        theoryEn = "File handling operations allow permanent data storage on secondary storage disk.",
                        theoryTe = "ఫైల్ హ్యాండ్లింగ్ ద్వారా డేటాను శాశ్వతంగా నిల్వ చేయవచ్చు.",
                        algorithmEn = listOf("Start", "Open file in 'w' mode", "fprintf data", "Close file", "Open file in 'r' mode", "fgets & print", "Close file", "Stop"),
                        algorithmTe = listOf("ప్రారంభం", "ఫైల్ ఓపెన్ చేయండి", "డేటా రాయండి", "ఫైల్ క్లోజ్ చేయండి", "ముగింపు"),
                        flowchartSteps = listOf("START", "fopen('lab.txt', 'w')", "fprintf()", "fclose()", "END"),
                        sampleCode = """
                            #include <stdio.h>
                            
                            int main() {
                                FILE *fp = fopen("lab.txt", "w");
                                if(fp != NULL) {
                                    fprintf(fp, "C Lab Experiments Completed\n");
                                    fclose(fp);
                                    printf("File created and written successfully.\n");
                                }
                                return 0;
                            }
                        """.trimIndent(),
                        sampleInput = "",
                        sampleOutput = "File created and written successfully.",
                        testCases = listOf(TestCase(1, "", "File created and written successfully.\n", false)),
                        vivaQuestions = listOf(
                            VivaQuestion(1, "What does fopen() return on error?", "fopen() ఫెయిల్ అయితే ఏమి రిటర్న్ చేస్తుంది?", "NULL pointer", "NULL పాయింటర్")
                        )
                    )
                )
            )
        )
    }

    // Sample Quizzes
    val quizzes: List<QuizModule> by lazy {
        listOf(
            QuizModule(
                id = "quiz_1",
                titleEn = "C Basics & Syntax Quiz",
                titleTe = "C ప్రాథమికాంశాలు మరియు సింటాక్స్ క్విజ్",
                timeLimitMinutes = 10,
                questions = listOf(
                    QuizQuestion(
                        id = 1,
                        questionEn = "Which header file is required for printf() and scanf()?",
                        questionTe = "printf() మరియు scanf() కోసం ఏ హెడర్ ఫైల్ అవసరం?",
                        optionsEn = listOf("stdlib.h", "stdio.h", "conio.h", "string.h"),
                        optionsTe = listOf("stdlib.h", "stdio.h", "conio.h", "string.h"),
                        correctIndex = 1,
                        explanationEn = "stdio.h stands for Standard Input Output header file.",
                        explanationTe = "stdio.h అంటే స్టాండర్డ్ ఇన్పుట్ అండ్ అవుట్పుట్ హెడర్ ఫైల్."
                    ),
                    QuizQuestion(
                        id = 2,
                        questionEn = "What is the format specifier for integer variable in scanf?",
                        questionTe = "scanf లో ఇంటిజర్ వేరియబుల్ కోసం ఉపయోగించే ఫార్మాట్ స్పెసిఫైయర్ ఏమిటి?",
                        optionsEn = listOf("%f", "%c", "%d", "%s"),
                        optionsTe = listOf("%f", "%c", "%d", "%s"),
                        correctIndex = 2,
                        explanationEn = "%d is used for signed decimal integers.",
                        explanationTe = "%d ని డెసిమల్ ఇంటిజర్ల కోసం ఉపయోగిస్తారు."
                    )
                )
            )
        )
    }

    // Sample Badges
    val initialBadges: List<UserBadge> by lazy {
        listOf(
            UserBadge("b_1", "First Program", "మొదటి ప్రోగ్రామ్", "Executed your first C code in compiler", "కంపైలర్‌లో మీ మొదటి C కోడ్ విజయవంతంగా రన్ చేశారు", "Code", true, System.currentTimeMillis()),
            UserBadge("b_2", "Lab Champion", "ల్యాబ్ ఛాంపియన్", "Completed 20 Virtual C Lab experiments", "20 వర్చువల్ C ల్యాబ్ ప్రయోగాలను పూర్తి చేశారు", "EmojiEvents", true, System.currentTimeMillis()),
            UserBadge("b_3", "Debugging Master", "డీబగ్గింగ్ మాస్టర్", "Fixed 10 compilation errors with AI Tutor", "AI ట్యూటర్ సహాయంతో 10 పొరపాట్లను సరిదిద్దారు", "BugReport", false, null),
            UserBadge("b_4", "Telugu C Scholar", "తెలుగు C స్కాలర్", "Learned 5 lessons using Telugu explanations", "తెలుగు వివరణలతో 5 పాఠాలు నేర్చుకున్నారు", "Translate", true, System.currentTimeMillis())
        )
    }
}
