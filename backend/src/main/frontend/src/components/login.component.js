import React, { Component } from "react";
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";
import Spinner from "./spinner.js"

import AuthService from "../services/auth.service";

import { withRouter } from '../common/with-router';

const required = value => {
  if (!value) {
    return (
      <div className="login-danger-alert" role="alert">
        To pole jest wymagane
      </div>
    );
  }
};

class Login extends Component {
  constructor(props) {
    super(props);
    this.handleLogin = this.handleLogin.bind(this);
    this.onChangeUsername = this.onChangeUsername.bind(this);
    this.onChangePassword = this.onChangePassword.bind(this);

    this.state = {
      username: "",
      password: "",
      loading: false,
      message: ""
    };
  }

  onChangeUsername(e) {
    this.setState({
      username: e.target.value
    });
  }

  onChangePassword(e) {
    this.setState({
      password: e.target.value
    });
  }

  handleLogin(e) {
    e.preventDefault();

    this.setState({
      message: "",
      loading: true
    });

    this.form.validateAll();

    if (this.checkBtn.context._errors.length === 0) {
      AuthService.login(this.state.username, this.state.password).then(
        () => {
          this.props.router.navigate("/");
          window.location.reload();
        },
        error => {
          const resMessage =
            (error.response &&
              error.response.data &&
              error.response.data.message) ||
            error.message ||
            error.toString();

          this.setState({
            loading: false,
            message: resMessage
          });
        }
      );
    } else {
      this.setState({
        loading: false
      });
    }
  }

  render() {
    return (
        <div className="container-login">
          <Form
            onSubmit={this.handleLogin}
            ref={c => {
              this.form = c;
            }}
            className='login-form'
          >
            <h3 className="form-header">Logowanie do Sudół</h3>
            <div className="form-group">
              <label htmlFor="username">Login</label>
              <Input
                type="text"
                id="username"
                className="form-control"
                name="username"
                value={this.state.username}
                onChange={this.onChangeUsername}
                validations={[required]}
                placeholder="Wprowadź login"
              />
            </div>

            <div className="form-group">
              <label htmlFor="password">Hasło</label>
              <Input
                type="password"
                id="password"
                className="form-control"
                name="password"
                value={this.state.password}
                onChange={this.onChangePassword}
                validations={[required]}
                placeholder="Wprowadź hasło"
              />
            </div>

            <div className="form-group">
              <button
                className="btn-loggin"
                disabled={this.state.loading}
              >
                <span>Zaloguj się</span>
              </button>
            </div>

            {this.state.message && (
              <div className="form-group">
                <div className="login-danger-alert" role="alert">
                  Nieprawidłowe dane logowania
                </div>
              </div>
            )}
            <CheckButton
              style={{ display: "none" }}
              ref={c => {
                this.checkBtn = c;
              }}
            />
          </Form>
        </div>
    );
  }
}

export default withRouter(Login);