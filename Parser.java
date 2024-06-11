//----------------------------------------------
// Author:     Quinn Trate
// Date:       March 27, 2024
// Class:      CMPSC 470 Section 1: Compilers
// Instructor: Dr. Hyuntae Na
// Purpose:    Parser Class that prints tokens
//             from the Lexical Analyzer. Uses
//             Parse Tree for Specific Errors
//----------------------------------------------

import java.util.*;

public class Parser
{
    public static final int ENDMARKER   =  0;
    public static final int LEXERROR    =  1;

    public static final int RETURN      = 11; // "return"
    public static final int BEGIN       = 12; // "begin"
    public static final int PRINT       = 13; // "print"
    public static final int WHILE       = 14; // "while"
    public static final int BOOL        = 15; // "boolean"
    public static final int ELSE        = 16; // "else"
    public static final int FUNC        = 17; // "function"
    public static final int SIZE        = 18; // "size"
    public static final int THEN        = 19; // "then"
    public static final int VOID        = 20; // "void"
    public static final int END         = 21; // "end"
    public static final int NEW         = 22; // "new"
    public static final int NUM         = 23; // "num"
    public static final int VAR         = 24; // "variable"
    public static final int IF          = 25; // "if"
    public static final int ASSIGN      = 26; // ":="
    public static final int TYPEOF      = 27; // "::"
    public static final int LPAREN      = 28; // "("
    public static final int RPAREN      = 29; // ")"
    public static final int LBRACKET    = 30; // "["
    public static final int RBRACKET    = 31; // "]"
    public static final int SEMI        = 32; // ";"
    public static final int COMMA       = 33; // ","
    public static final int DOT         = 34; // "."
    public static final int TERMOP      = 35; // "+", "-", "and"
    public static final int EXPROP      = 36; // "+", "-", "or"
    public static final int RELOP       = 37; // "=", "<", ">", "<=", ">=", "<>"
    public static final int NUM_LIT     = 38; // {num}
    public static final int BOOL_LIT    = 39; // "true", "false"
    public static final int IDENT       = 40; // {identifier}

    // Constructor Class for Tokens
    public class Token
    {
        public int       type;
        public ParserVal attr;
        public Token(int type, ParserVal attr)
        {
            this.type   = type;
            this.attr   = attr;
        }
    }

    public ParserVal yylval;
    Token _token;
    Lexer _lexer;
    Compiler _compiler;
    public ParseTree.Program _parsetree;
    public String            _errormsg;

    // Hashmap for Tokens
    HashMap<Integer, Integer> symbols = new HashMap<Integer, Integer>();

    // Parser Constructor
    public Parser(java.io.Reader r, Compiler compiler) throws Exception
    {
        _compiler  = compiler;
        _parsetree = null;
        _errormsg  = null;
        _lexer     = new Lexer(r, this);
        // _token is Initially Null
        _token     = null;
        // Make _token to Point to the First Token by Calling Advance()
        Advance();

        // Fill Hashmap with Tokens
        symbols.put(0, ENDMARKER);
        symbols.put(1, LEXERROR);
        symbols.put(11, RETURN);
        symbols.put(12, BEGIN);
        symbols.put(13, PRINT);
        symbols.put(14, WHILE);
        symbols.put(15, BOOL);
        symbols.put(16, ELSE);
        symbols.put(17, FUNC);
        symbols.put(18, SIZE);
        symbols.put(19, THEN);
        symbols.put(20, VOID);
        symbols.put(21, END);
        symbols.put(22, NEW);
        symbols.put(23, NUM);
        symbols.put(24, VAR);
        symbols.put(25, IF);
        symbols.put(26, ASSIGN);
        symbols.put(27, TYPEOF);
        symbols.put(28, LPAREN);
        symbols.put(29, RPAREN);
        symbols.put(30, LBRACKET);
        symbols.put(31, RBRACKET);
        symbols.put(32, SEMI);
        symbols.put(33, COMMA);
        symbols.put(34, DOT);
        symbols.put(35, TERMOP);
        symbols.put(36, EXPROP);
        symbols.put(37, RELOP);
        symbols.put(38, NUM_LIT);
        symbols.put(39, BOOL_LIT);
        symbols.put(40, IDENT);
    }

    // Advance to Next Token
    public void Advance() throws Exception
    {
        // Get Next/First Token from Lexer
        int token_type = _lexer.yylex();
        // If  0 => Token is Endmarker
        if(token_type ==  0)
            _token = new Token(ENDMARKER , null);
        // If -1 => There is a Lexical Error
        else if(token_type == -1)
            _token = new Token(LEXERROR  , yylval);
        // Otherwise, Set Up _token
        else
            _token = new Token(token_type, yylval);
    }

    // Match the Lexemes to Keywords
    public String Match(int token_type) throws Exception
    {
        boolean match = (token_type == _token.type);
        String lexeme = "";
        if(_token.attr != null)
            lexeme = (String)_token.attr.obj;
        String inputLexeme = "";

        // Sets inputLexeme to a Keyword
        switch(token_type)
        {
            case 11:
                inputLexeme = "return";
                break;
            case 12:
                inputLexeme = "begin";
                break;
            case 13:
                inputLexeme = "print";
                break;
            case 14:
                inputLexeme = "while";
                break;
            case 15:
                inputLexeme = "bool";
                break;
            case 16:
                inputLexeme = "else";
                break;
            case 17:
                inputLexeme = "func";
                break;
            case 18:
                inputLexeme = "size";
                break;
            case 19:
                inputLexeme = "then";
                break;
            case 20:
                inputLexeme = "void";
                break;
            case 21:
                inputLexeme = "end";
                break;
            case 22:
                inputLexeme = "new";
                break;
            case 23:
                inputLexeme = "num";
                break;
            case 24:
                inputLexeme = "var";
                break;
            case 25:
                inputLexeme = "if";
                break;
            case 26:
                inputLexeme = ":=";
                break;
            case 27:
                inputLexeme = "::";
                break;
            case 28:
                inputLexeme = "(";
                break;
            case 29:
                inputLexeme = ")";
                break;
            case 30:
                inputLexeme = "[";
                break;
            case 31:
                inputLexeme = "]";
                break;
            case 32:
                inputLexeme = ";";
                break;
            case 33:
                inputLexeme = ",";
                break;
            case 34:
                inputLexeme = ".";
            break;
        }

        // If Token Does not Match
        if(match == false)
            throw new Exception("\"" + inputLexeme + "\" is expected instead of \"" + _token.attr.obj + "\" at " + _lexer.lineno + ":" + _lexer.column + ".");  // throw exception (indicating parsing error in this assignment)

        // If Token is not Endmarker, Make Token Point Next Token in Unput by Calling Advance()
        if(_token.type != ENDMARKER)
            Advance();

        return lexeme;
    }

    public int yyparse() throws Exception
    {
        try
        {
            _parsetree = program();
            return 0;
        }
        catch(Exception e)
        {
            _errormsg = e.getMessage();
            return -1;
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    //    program     -> decl_list
    //    decl_list   -> decl_list'
    //    decl_list'  -> fun_decl decl_list'
    //    fun_decl    -> FUNC IDENT TYPEOF prim_type LPAREN params RPAREN BEGIN local_decls stmt_list END
    //    params      -> param_list
    //                | eps
    //    param_list  -> param param_list'
    //    param_list' -> COMMA param param_list'
    //                | eps
    //    param       -> IDENT TYPEOF type_spec
    //    type_spec   -> prim_type type_spec'
    //    type_spec'  -> LBRACKET RBRACKET
    //                | eps
    //    prim_type   -> NUM
    //                | BOOL
    //                | VOID
    //    local_decls -> local_decls'
    //    local_decls'-> local_decl local_decls'
    //                | eps
    //    local_decl  -> VAR IDENT TYPEOF type_spec SEMI
    //    stmt_list   -> stmt_list'
    //    stmt_list'  -> stmt stmt_list'
    //                | eps
    //    stmt        -> assign_stmt
    //                | print_stmt
    //                | return_stmt
    //                | if_stmt
    //                | while_stmt
    //                | compound_stmt
    //    assign_stmt -> IDENT ASSIGN expr SEMI
    //    print_stmt  -> PRINT expr SEMI
    //    return_stmt -> RETURN expr SEMI
    //    if_stmt     -> IF LPAREN expr RPAREN THEN stmt_list ELSE stmt_list END
    //    while_stmt  -> WHILE LPAREN expr RPAREN BEGIN stmt_list END
    //    compound_stmt -> BEGIN local_decls stmt_list END
    //    args        -> arg_list
    //                | eps
    //    arg_list    -> expr arg_list'
    //    arg_list'   -> COMMA expr arg_list'
    //                | eps
    //    expr        -> term expr'
    //    expr'       -> EXPROP term expr'
    //                | RELOP term expr'
    //                | eps
    //    term        -> factor term'
    //    term'       -> TERMOP factor term'
    //                | eps
    //    factor      -> IDENT factor'
    //                | LPAREN expr RPAREN
    //                | NUM_LIT
    //                | BOOL_LIT
    //                | NEW prim_type LBRACKET expr RBRACKET
    //    factor'     -> LPAREN args RPAREN
    //                | LBRACKET expr RBRACKET
    //                | DOT SIZE
    //                | eps
    ///////////////////////////////////////////////////

    // program -> decl_list
    public ParseTree.Program program() throws Exception
    {
        switch(_token.type)
        {
            case FUNC:
            case ENDMARKER:
                List<ParseTree.FuncDecl> funcs = decl_list();
                String v1 = Match(ENDMARKER);
                return new ParseTree.Program(funcs);
        }
        throw new Exception("No matching production in program at " + _lexer.lineno + ":" + _lexer.column + ".");
    }

    // decl_list -> decl_list'
    public List<ParseTree.FuncDecl> decl_list() throws Exception
    {
        switch(_token.type)
        {
            case FUNC:
            case ENDMARKER:
                return decl_list_();
        }
        throw new Exception("No matching production in decl_list at " + _lexer.lineno + ":" + _lexer.column + ".");
    }

    // decl_list' -> fun_decl decl_list'  |  eps
    public List<ParseTree.FuncDecl> decl_list_() throws Exception
    {
        switch(_token.type)
        {
            case FUNC:
                ParseTree.FuncDecl       v1 = fun_decl  ();
                List<ParseTree.FuncDecl> v2 = decl_list_();
                v2.add(0, v1);
                return v2;
            case ENDMARKER:
                return new ArrayList<ParseTree.FuncDecl>();
        }
        throw new Exception("No matching production in decl_list' at " + _lexer.lineno + ":" + _lexer.column + ".");
    }

    // fun_decl -> FUNC IDENT TYPEOF prim_type LPAREN params RPAREN BEGIN local_decls stmt_list END
    public ParseTree.FuncDecl fun_decl() throws Exception
    {
        switch(_token.type)
        {
            case FUNC:
                String                    v01 = Match(FUNC  );
                String                    v02 = Match(IDENT );//
                String                    v03 = Match(TYPEOF);
                ParseTree.PrimType        v04 = prim_type(  );//
                String                    v05 = Match(LPAREN);
                List<ParseTree.Param>     v06 = params(     );//
                String                    v07 = Match(RPAREN);
                String                    v08 = Match(BEGIN );
                List<ParseTree.LocalDecl> v09 = local_decls();//
                List<ParseTree.Stmt>      v10 = stmt_list(  );//
                String                    v11 = Match(END   );
                return new ParseTree.FuncDecl(v02, v04, v06, v09, v10);
        }
        throw new Exception("No matching production in fun_decl at " + _lexer.lineno + ":" + _lexer.column + ".");
    }

    // params -> param_list | eps
    public List<ParseTree.Param> params() throws Exception
    {
        switch(_token.type)
        {
            case RPAREN:
                return new ArrayList<ParseTree.Param>();
            case IDENT:
                List<ParseTree.Param> v1 = param_list();
                return v1;
        }
        throw new Exception("No matching production in params at " + _lexer.lineno + ":" + _lexer.column + ".");
    }

    // param_list -> param param_list'
    public List<ParseTree.Param> param_list() throws Exception
    {
        switch(_token.type)
        {
            case IDENT:
                ParseTree.Param v1 = param();
                List<ParseTree.Param> v2 = param_list_();
                v2.add(0, v1);
                return v2;
        }
        throw new Exception("No matching production in param_list at " + _lexer.lineno + ":" + _lexer.column + ".");
    }

    // param_list' -> COMMA param param_list' | eps
    public List<ParseTree.Param> param_list_() throws Exception
    {
        switch(_token.type)
        {
            case RPAREN:
                return new ArrayList<ParseTree.Param>();
            case COMMA:
                String v1 = Match(COMMA);
                ParseTree.Param v2 = param();
                List<ParseTree.Param> v3 = param_list_();
                v3.add(0, v2);
                return v3;
        }
        throw new Exception("No matching production in param_list' at " + _lexer.lineno + ":" + _lexer.column + ".");
    }

    // param -> IDENT TYPEOF type_spec
    public ParseTree.Param param() throws Exception
    {
        switch(_token.type)
        {
            case IDENT:
                String v1 = Match(IDENT);
                String v2 = Match(TYPEOF);
                ParseTree.TypeSpec v3 = type_spec();
                return new ParseTree.Param(v1, v3);
        }
        throw new Exception("No matching production in param at " + _lexer.lineno + ":" + _lexer.column + ".");
    }

    // type_spec -> prim_type type_spec'
    public ParseTree.TypeSpec type_spec() throws Exception
    {
        switch(_token.type)
        {
            case BOOL:
                ParseTree.PrimType v1 = prim_type();
                ParseTree.TypeSpec_ v2 = type_spec_();
                return new ParseTree.TypeSpec(v1, v2);
            case VOID:
                ParseTree.PrimType v3 = prim_type();
                ParseTree.TypeSpec_ v4 = type_spec_();
                return new ParseTree.TypeSpec(v3, v4);
            case NUM:
                ParseTree.PrimType v5 = prim_type();
                ParseTree.TypeSpec_ v6 = type_spec_();
                return new ParseTree.TypeSpec(v5, v6);
        }
        throw new Exception("No matching production in type_spec at " + _lexer.lineno + ":" + _lexer.column + ".");
    }

    // type_spec' -> LBRACKET RBRACKET | eps
    public ParseTree.TypeSpec_ type_spec_() throws Exception
    {
        switch(_token.type)
        {
            case RPAREN:
                return new ParseTree.TypeSpec_Value();
            case LBRACKET:
                String v1 = Match(LBRACKET);
                String v2 = Match(RBRACKET);
                return new ParseTree.TypeSpec_Array();
            case SEMI:
                return new ParseTree.TypeSpec_Value();
            case COMMA:
                return new ParseTree.TypeSpec_Value();
        }
        throw new Exception("No matching production in type_spec' at " + _lexer.lineno + ":" + _lexer.column + ".");
    }

    // prim_type -> NUM | BOOL | VOID
    public ParseTree.PrimType prim_type() throws Exception
    {
        switch(_token.type)
        {
            case NUM:
            {
                String v1 = Match(NUM);
                return new ParseTree.PrimTypeNum();
            }
            case BOOL:
            {
                String v2 = Match(BOOL);
                return new ParseTree.PrimTypeBool();
            }
            case VOID:
            {
                String v3 = Match(VOID);
                return new ParseTree.PrimTypeVoid();
            }
        }
        throw new Exception("No matching production in prim_type at " + _lexer.lineno + ":" + _lexer.column + ".");
    }

    // local_decls -> local_decls'
    public List<ParseTree.LocalDecl> local_decls() throws Exception
    {
        switch(_token.type)
        {
            case RETURN:
                List<ParseTree.LocalDecl> v1 = local_decls_();
                return v1;
            case BEGIN:
                List<ParseTree.LocalDecl> v2 = local_decls_();
                return v2;
            case PRINT:
                List<ParseTree.LocalDecl> v3 = local_decls_();
                return v3;
            case WHILE:
                List<ParseTree.LocalDecl> v4 = local_decls_();
                return v4;
            case END:
                List<ParseTree.LocalDecl> v5 = local_decls_();
                return v5;
            case VAR:
                List<ParseTree.LocalDecl> v6 = local_decls_();
                return v6;
            case IF:
                List<ParseTree.LocalDecl> v7 = local_decls_();
                return v7;
            case IDENT:
                List<ParseTree.LocalDecl> v8 = local_decls_();
                return v8;
        }
        throw new Exception("No matching production in local_decls at " + _lexer.lineno + ":" + _lexer.column + ".");
    }

    // local_decls'-> local_decl local_decls' | eps
    public List<ParseTree.LocalDecl> local_decls_() throws Exception
    {
        switch(_token.type)
        {
            case RETURN:
                return new ArrayList<ParseTree.LocalDecl>();
            case BEGIN:
                return new ArrayList<ParseTree.LocalDecl>();
            case PRINT:
                return new ArrayList<ParseTree.LocalDecl>();
            case WHILE:
                return new ArrayList<ParseTree.LocalDecl>();
            case END:
                return new ArrayList<ParseTree.LocalDecl>();
            case VAR:
                ParseTree.LocalDecl v1 = local_decl();
                List<ParseTree.LocalDecl> v2 = local_decls_();
                v2.add(0, v1);
                return v2;
            case IF:
                return new ArrayList<ParseTree.LocalDecl>();
            case IDENT:
                return new ArrayList<ParseTree.LocalDecl>();
        }
        throw new Exception("No matching production in local_decls' at " + _lexer.lineno + ":" + _lexer.column + ".");
    }

    // local_decl -> VAR IDENT TYPEOF type_spec SEMI
    public ParseTree.LocalDecl local_decl() throws Exception
    {
        switch(_token.type)
        {
            case VAR:
                String v1 = Match(VAR);
                String v2 = Match(IDENT);
                String v3 = Match(TYPEOF);
                ParseTree.TypeSpec v4 = type_spec();
                String v5 = Match(SEMI);
                return new ParseTree.LocalDecl(v2, v4);
        }
        throw new Exception("No matching production in local_decl at " + _lexer.lineno + ":" + _lexer.column + ".");
    }

    // stmt_list -> stmt_list'
    public List<ParseTree.Stmt> stmt_list() throws Exception
    {
        switch(_token.type)
        {
            case RETURN:
                List<ParseTree.Stmt> v1 = stmt_list_();
                return v1;
            case BEGIN:
                List<ParseTree.Stmt> v2 = stmt_list_();
                return v2;
            case PRINT:
                List<ParseTree.Stmt> v3 = stmt_list_();
                return v3;
            case WHILE:
                List<ParseTree.Stmt> v4 = stmt_list_();
                return v4;
            case ELSE:
                List<ParseTree.Stmt> v5 = stmt_list_();
                return v5;
            case END:
                List<ParseTree.Stmt> v6 = stmt_list_();
                return v6;
            case IF:
                List<ParseTree.Stmt> v7 = stmt_list_();
                return v7;
            case IDENT:
                List<ParseTree.Stmt> v8 = stmt_list_();
                return v8;
        }
        throw new Exception("No matching production in stmt_list at " + _lexer.lineno + ":" + _lexer.column + ".");
    }

    // stmt_list' -> stmt stmt_list' | eps
    public List<ParseTree.Stmt> stmt_list_() throws Exception
    {
        switch(_token.type)
        {
            case RETURN:
                ParseTree.Stmt v1 = stmt();
                List<ParseTree.Stmt> v2 = stmt_list_();
                v2.add(0, v1);
                return v2;
            case BEGIN:
                ParseTree.Stmt v3 = stmt();
                List<ParseTree.Stmt> v4 = stmt_list_();
                v4.add(0, v3);
                return v4;
            case PRINT:
                ParseTree.Stmt v5 = stmt();
                List<ParseTree.Stmt> v6 = stmt_list_();
                v6.add(0, v5);
                return v6;
            case WHILE:
                ParseTree.Stmt v7 = stmt();
                List<ParseTree.Stmt> v8 = stmt_list_();
                v8.add(0, v7);
                return v8;
            case ELSE:
                return new ArrayList<ParseTree.Stmt>();
            case END:
                return new ArrayList<ParseTree.Stmt>();
            case IF:
                ParseTree.Stmt v9 = stmt();
                List<ParseTree.Stmt> v10 = stmt_list_();
                v10.add(0, v9);
                return v10;
            case IDENT:
                ParseTree.Stmt v11 = stmt();
                List<ParseTree.Stmt> v12 = stmt_list_();
                v12.add(0, v11);
                return v12;
        }
        throw new Exception("No matching production in stmt_list' at " + _lexer.lineno + ":" + _lexer.column + ".");
    }

    // stmt -> assign_stmt | print_stmt | return_stmt | if_stmt | while_stmt | compound_stmt
    public ParseTree.Stmt stmt() throws Exception
    {
        switch(_token.type)
        {
            case RETURN:
                ParseTree.StmtReturn v1 = return_stmt();
                return v1;
            case BEGIN:
                ParseTree.StmtCompound v2 = compound_stmt();
                return v2;
            case PRINT:
                ParseTree.StmtPrint v3 = print_stmt();
                return v3;
            case WHILE:
                ParseTree.StmtWhile v4 = while_stmt();
                return v4;
            case IF:
                ParseTree.StmtIf v5 = if_stmt();
                return v5;
            case IDENT:
                ParseTree.StmtAssign v6 = assign_stmt();
                return v6;
        }
        throw new Exception("No matching production in stmt at " + _lexer.lineno + ":" + _lexer.column + ".");
    }

    // assign_stmt -> IDENT ASSIGN expr SEMI
    public ParseTree.StmtAssign assign_stmt() throws Exception
    {
        switch(_token.type)
        {
            case IDENT:
                String v1 = Match(IDENT);
                String v2 = Match(ASSIGN);
                ParseTree.Expr v3 = expr();
                String v4 = Match(SEMI);
                return new ParseTree.StmtAssign(v1, v3);
        }
        throw new Exception("No matching production in assign_stmt at " + _lexer.lineno + ":" + _lexer.column + ".");
    }

    // print_stmt -> PRINT expr SEMI
    public ParseTree.StmtPrint print_stmt() throws Exception
    {
        switch(_token.type)
        {
            case PRINT:
                String v1 = Match(PRINT);
                ParseTree.Expr v2 = expr();
                String v3 = Match(SEMI);
                return new ParseTree.StmtPrint(v2);
        }
        throw new Exception("No matching production in print_stmt at " + _lexer.lineno + ":" + _lexer.column + ".");
    }

    // return_stmt -> RETURN expr SEMI
    public ParseTree.StmtReturn return_stmt() throws Exception
    {
        switch(_token.type)
        {
            case RETURN:
                String v1 = Match(RETURN);
                ParseTree.Expr v2 = expr();
                String v3 = Match(SEMI);
                return new ParseTree.StmtReturn(v2);
        }
        throw new Exception("No matching production in return_stmt at " + _lexer.lineno + ":" + _lexer.column + ".");
    }

    // if_stmt -> IF LPAREN expr RPAREN THEN stmt_list ELSE stmt_list END
    public ParseTree.StmtIf if_stmt() throws Exception
    {
        switch(_token.type)
        {
            case IF:
                String v1 = Match(IF);
                String v2 = Match(LPAREN);
                ParseTree.Expr v3 = expr();
                String v4 = Match(RPAREN);
                String v5 = Match(THEN);
                List<ParseTree.Stmt> v6 = stmt_list();
                String v7 = Match(ELSE);
                List<ParseTree.Stmt> v8 = stmt_list();
                String v9 = Match(END);
                return new ParseTree.StmtIf(v3, v6, v8);
        }
        throw new Exception("No matching production in if_stmt at " + _lexer.lineno + ":" + _lexer.column + ".");
    }

    // while_stmt -> WHILE LPAREN expr RPAREN BEGIN stmt_list END
    public ParseTree.StmtWhile while_stmt() throws Exception
    {
        switch(_token.type)
        {
            case WHILE:
                String v1 = Match(WHILE);
                String v2 = Match(LPAREN);
                ParseTree.Expr v3 = expr();
                String v4 = Match(RPAREN);
                String v5 = Match(BEGIN);
                List<ParseTree.Stmt> v6 = stmt_list();
                String v7 = Match(END);
                return new ParseTree.StmtWhile(v3, v6);
        }
        throw new Exception("No matching production in while_stmt at " + _lexer.lineno + ":" + _lexer.column + ".");
    }

    // compound_stmt -> BEGIN local_decls stmt_list END
    public ParseTree.StmtCompound compound_stmt() throws Exception
    {
        switch(_token.type)
        {
            case BEGIN:
                String v1 = Match(BEGIN);
                List<ParseTree.LocalDecl> v2 = local_decls();
                List<ParseTree.Stmt> v3 = stmt_list();
                String v4 = Match(END);
                return new ParseTree.StmtCompound(v2, v3);
        }
        throw new Exception("No matching production in compound_stmt at " + _lexer.lineno + ":" + _lexer.column + ".");
    }

    // args -> arg_list | eps
    public List<ParseTree.Arg> args() throws Exception
    {
        switch(_token.type)
        {
            case NEW:
                List<ParseTree.Arg> v1 = arg_list();
                return v1;
            case LPAREN:
                List<ParseTree.Arg> v2 = arg_list();
                return v2;
            case RPAREN:
                return new ArrayList<ParseTree.Arg>();
            case NUM_LIT:
                List<ParseTree.Arg> v3 = arg_list();
                return v3;
            case BOOL_LIT:
                List<ParseTree.Arg> v4 = arg_list();
                return v4;
            case IDENT:
                List<ParseTree.Arg> v5 = arg_list();
                return v5;
        }
        throw new Exception("No matching production in args at " + _lexer.lineno + ":" + _lexer.column + ".");
    }

    // arg_list -> expr arg_list'
    public List<ParseTree.Arg> arg_list() throws Exception
    {
        switch(_token.type)
        {
            case NEW:
                ParseTree.Expr v1 = expr();
                List<ParseTree.Arg> v2 = arg_list_();
                v2.add(0, new ParseTree.Arg(v1));
                return v2;
            case LPAREN:
                ParseTree.Expr v3 = expr();
                List<ParseTree.Arg> v4 = arg_list_();
                v4.add(0, new ParseTree.Arg(v3));
                return v4;
            case NUM_LIT:
                ParseTree.Expr v5 = expr();
                List<ParseTree.Arg> v6 = arg_list_();
                v6.add(0, new ParseTree.Arg(v5));
                return v6;
            case BOOL_LIT:
                ParseTree.Expr v7 = expr();
                List<ParseTree.Arg> v8 = arg_list_();
                v8.add(0, new ParseTree.Arg(v7));
                return v8;
            case IDENT:
                ParseTree.Expr v9 = expr();
                List<ParseTree.Arg> v10 = arg_list_();
                v10.add(0, new ParseTree.Arg(v9));
                return v10;
        }
        throw new Exception("No matching production in arg_list at " + _lexer.lineno + ":" + _lexer.column + ".");
    }

    // arg_list' -> COMMA expr arg_list' | eps
    public List<ParseTree.Arg> arg_list_() throws Exception
    {
        switch(_token.type)
        {
            case RPAREN:
                return new ArrayList<ParseTree.Arg>();
            case COMMA:
                String v1 = Match(COMMA);
                ParseTree.Expr v2 = expr();
                List<ParseTree.Arg> v3 = arg_list_();
                v3.add(0, new ParseTree.Arg(v2));
                return v3;
        }
        throw new Exception("No matching production in arg_list' at " + _lexer.lineno + ":" + _lexer.column + ".");
    }

    // expr -> term expr'
    public ParseTree.Expr expr() throws Exception
    {
        switch(_token.type)
        {
            case NEW:
                ParseTree.Term v1 = term();
                ParseTree.Expr_ v2 = expr_();
                return new ParseTree.Expr(v1, v2);
            case LPAREN:
                ParseTree.Term v3 = term();
                ParseTree.Expr_ v4 = expr_();
                return new ParseTree.Expr(v3, v4);
            case NUM_LIT:
                ParseTree.Term v5 = term();
                ParseTree.Expr_ v6 = expr_();
                return new ParseTree.Expr(v5, v6);
            case BOOL_LIT:
                ParseTree.Term v7 = term();
                ParseTree.Expr_ v8 = expr_();
                return new ParseTree.Expr(v7, v8);
            case IDENT:
                ParseTree.Term v9 = term();
                ParseTree.Expr_ v10 = expr_();
                return new ParseTree.Expr(v9, v10);
        }
        throw new Exception("No matching production in expr at " + _lexer.lineno + ":" + _lexer.column + ".");
    }

    // expr' -> EXPROP term expr' | RELOP term expr' | eps
    public ParseTree.Expr_ expr_() throws Exception
    {
        switch(_token.type)
        {
            case RPAREN:
                return null;
            case RBRACKET:
                return null;
            case SEMI:
                return null;
            case COMMA:
                return null;
            case EXPROP:
                String v1 = Match(EXPROP);
                ParseTree.Term v2 = term();
                ParseTree.Expr_ v3 = expr_();
                return new ParseTree.Expr_(v1, v2, v3);
            case RELOP:
                String v4 = Match(RELOP);
                ParseTree.Term v5 = term();
                ParseTree.Expr_ v6 = expr_();
                return new ParseTree.Expr_(v4, v5, v6);
        }
        throw new Exception("No matching production in expr' at " + _lexer.lineno + ":" + _lexer.column + ".");
    }

    // term -> factor term'
    public ParseTree.Term term() throws Exception
    {
        switch(_token.type)
        {
            case NEW:
                ParseTree.Factor v1 = factor();
                ParseTree.Term_ v2 = term_();
                return new ParseTree.Term(v1, v2);
            case LPAREN:
                ParseTree.Factor v3 = factor();
                ParseTree.Term_ v4 = term_();
                return new ParseTree.Term(v3, v4);
            case NUM_LIT:
                ParseTree.Factor v5 = factor();
                ParseTree.Term_ v6 = term_();
                return new ParseTree.Term(v5, v6);
            case BOOL_LIT:
                ParseTree.Factor v7 = factor();
                ParseTree.Term_ v8 = term_();
                return new ParseTree.Term(v7, v8);
            case IDENT:
                ParseTree.Factor v9 = factor();
                ParseTree.Term_ v10 = term_();
                return new ParseTree.Term(v9, v10);
        }
        throw new Exception("No matching production in term at " + _lexer.lineno + ":" + _lexer.column + ".");
    }

    // term' -> TERMOP factor term' | eps
    public ParseTree.Term_ term_() throws Exception
    {
        switch(_token.type)
        {
            case RPAREN:
                return null;
            case RBRACKET:
                return null;
            case SEMI:
                return null;
            case COMMA:
                return null;
            case TERMOP:
                String v1 = Match(TERMOP);
                ParseTree.Factor v2 = factor();
                ParseTree.Term_ v3 = term_();
                return new ParseTree.Term_(v1, v2, v3);
            case EXPROP:
                return null;
            case RELOP:
                return null;
        }
        throw new Exception("No matching production in term' at " + _lexer.lineno + ":" + _lexer.column + ".");
    }

    // factor -> IDENT factor' | LPAREN expr RPAREN | NUM_LIT | BOOL_LIT | NEW prim_type LBRACKET expr RBRACKET
    public ParseTree.Factor factor() throws Exception
    {
        switch(_token.type)
        {
            case NEW:
                String v1 = Match(NEW);
                ParseTree.PrimType v2 = prim_type();
                String v3 = Match(LBRACKET);
                ParseTree.Expr v4 = expr();
                String v5 = Match(RBRACKET);
                return new ParseTree.FactorNew(v2, v4);
            case LPAREN:
                String v6 = Match(LPAREN);
                ParseTree.Expr v7 = expr();
                String v8 = Match(RPAREN);
                return new ParseTree.FactorParen(v7);
            case NUM_LIT:
                String v9 = Match(NUM_LIT);
                return new ParseTree.FactorNumLit(Double.parseDouble(v9));
            case BOOL_LIT:
                String v10 = Match(BOOL_LIT);
                return new ParseTree.FactorBoolLit(Boolean.parseBoolean(v10));
            case IDENT:
                String v11 = Match(IDENT);
                ParseTree.Factor_ v12 = factor_();
                return new ParseTree.FactorIdentExt(v11, v12);
        }
        throw new Exception("No matching production in factor at " + _lexer.lineno + ":" + _lexer.column + ".");
    }

    // factor' -> LPAREN args RPAREN | LBRACKET expr RBRACKET | DOT SIZE | eps
    public ParseTree.Factor_ factor_() throws Exception
    {
        switch(_token.type)
        {
            case LPAREN:
                String v1 = Match(LPAREN);
                List<ParseTree.Arg> v2 = args();
                String v3 = Match(RPAREN);
                return new ParseTree.FactorIdent_ParenArgs(v2);
            case RPAREN:
                return new ParseTree.FactorIdent_Eps();
            case LBRACKET:
                String v4 = Match(LBRACKET);
                ParseTree.Expr v5 = expr();
                String v6 = Match(RBRACKET);
                return new ParseTree.FactorIdent_BrackExpr(v5);
            case RBRACKET:
                return new ParseTree.FactorIdent_Eps();
            case SEMI:
                return new ParseTree.FactorIdent_Eps();
            case COMMA:
                return new ParseTree.FactorIdent_Eps();
            case DOT:
                String v7 = Match(DOT);
                String v8 = Match(SIZE);
                return new ParseTree.FactorIdent_DotSize();
            case TERMOP:
                return new ParseTree.FactorIdent_Eps();
            case EXPROP:
                return new ParseTree.FactorIdent_Eps();
            case RELOP:
                return new ParseTree.FactorIdent_Eps();
        }
        throw new Exception("No matching production in factor' at " + _lexer.lineno + ":" + _lexer.column + ".");
    }
}