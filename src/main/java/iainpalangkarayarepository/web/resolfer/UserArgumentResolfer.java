package iainpalangkarayarepository.web.resolfer;

//@Component
//public class UserArgumentResolfer implements HandlerMethodArgumentResolver {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Override
//    public boolean supportsParameter(MethodParameter parameter) {
//        return User.class.equals(parameter.getParameterType());
//    }
//
//    @Override
//    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//        HttpServletRequest servletRequest = (HttpServletRequest) webRequest.getNativeRequest();
//        String token = jwtUtil.resolveToken(servletRequest);
//        if (token == null) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
//        }
//        Claims claims = jwtUtil.parseJwtClaims(token);
//
//        User user = userRepository.findByUsername(claims.getSubject())
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));
//
////        if (claims.getExpiration() < System.currentTimeMillis()) {
////            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
////        }
//        return user;
//    }
//}
