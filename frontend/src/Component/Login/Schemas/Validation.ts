import * as Yup from 'yup'

// Montagem do schema de validação do formulário de login do usuário

const validationSchema = Yup.object().shape({
  username: Yup.string()
    .trim('The username cannot include leading and trailing spaces')
    .strict(true)
    .min(2, 'Too Short!')
    .max(50, 'Too Long!')
    .required('Required'),
  password: Yup.string()
    .min(8, 'Too Short!')
    .max(20, 'Too Long!')
    .required('Required'),
})

export default validationSchema
